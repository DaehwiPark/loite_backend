package com.boot.loiteBackend.common.file;

import com.jcraft.jsch.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.Properties;

@Slf4j
@RequiredArgsConstructor
public class SftpUploader {

    private final SftpProperties props;

    private Session createSession() throws JSchException {
        JSch jsch = new JSch();
        Session session = jsch.getSession(props.getUser(), props.getHost(), props.getPort());
        session.setPassword(props.getPassword());

        Properties cfg = new Properties();
        cfg.put("StrictHostKeyChecking", "no"); // 운영 환경에서는 yes + known_hosts 권장
        session.setConfig(cfg);
        session.connect(15000);
        return session;
    }

    public void upload(InputStream in, String remoteDir, String filename) throws Exception {
        Session session = createSession();
        ChannelSftp sftp = (ChannelSftp) session.openChannel("sftp");
        sftp.connect(10000);

        mkdirs(sftp, remoteDir);

        try {
            sftp.put(in, remoteDir.replaceAll("/+$", "") + "/" + filename);
        } finally {
            safeDisconnect(sftp, session);
        }
    }

    public void upload(org.springframework.web.multipart.MultipartFile file,
                       String remoteDir, String filename) throws Exception {
        try (InputStream in = file.getInputStream()) {
            upload(in, remoteDir, filename);
        }
    }

    public boolean delete(String remotePath) {
        Session session = null;
        ChannelSftp sftp = null;
        try {
            session = createSession();
            sftp = (ChannelSftp) session.openChannel("sftp");
            sftp.connect(10000);
            sftp.rm(remotePath);
            return true;
        } catch (Exception e) {
            log.error("SFTP 삭제 실패: {}", remotePath, e);
            return false;
        } finally {
            safeDisconnect(sftp, session);
        }
    }

    private static void mkdirs(ChannelSftp sftp, String path) throws SftpException {
        String[] parts = path.replace('\\','/').split("/");
        String cur = "";
        for (String p : parts) {
            if (p.isEmpty()) continue;
            cur += "/" + p;
            try { sftp.cd(cur); } catch (SftpException e) { sftp.mkdir(cur); }
        }
    }

    private static void safeDisconnect(ChannelSftp sftp, Session session) {
        try { if (sftp != null) sftp.disconnect(); } catch (Exception ignore) {}
        try { if (session != null) session.disconnect(); } catch (Exception ignore) {}
    }
}