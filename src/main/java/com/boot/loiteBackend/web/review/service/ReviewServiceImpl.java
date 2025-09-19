package com.boot.loiteBackend.web.review.service;

import com.boot.loiteBackend.admin.product.product.entity.AdminProductEntity;
import com.boot.loiteBackend.admin.product.product.entity.AdminProductImageEntity;
import com.boot.loiteBackend.admin.product.product.repository.AdminProductImageRepository;
import com.boot.loiteBackend.admin.product.product.repository.AdminProductRepository;
import com.boot.loiteBackend.common.file.FileService;
import com.boot.loiteBackend.common.file.FileUploadResult;
import com.boot.loiteBackend.domain.user.general.entity.UserEntity;
import com.boot.loiteBackend.web.order.dto.OrderAdditionalResponseDto;
import com.boot.loiteBackend.web.order.dto.OrderGiftResponseDto;
import com.boot.loiteBackend.web.order.dto.OrderOptionResponseDto;
import com.boot.loiteBackend.web.order.entity.OrderEntity;
import com.boot.loiteBackend.web.order.entity.OrderItemEntity;
import com.boot.loiteBackend.web.order.enums.OrderStatus;
import com.boot.loiteBackend.web.order.repository.OrderItemRepository;
import com.boot.loiteBackend.web.order.repository.OrderRepository;
import com.boot.loiteBackend.web.review.dto.*;
import com.boot.loiteBackend.web.review.entity.ReviewEntity;
import com.boot.loiteBackend.web.review.entity.ReviewHelpfulEntity;
import com.boot.loiteBackend.web.review.entity.ReviewMediaEntity;
import com.boot.loiteBackend.web.review.repository.ReviewHelpfulRepository;
import com.boot.loiteBackend.web.review.repository.ReviewMediaRepository;
import com.boot.loiteBackend.web.review.repository.ReviewRepository;
import com.boot.loiteBackend.web.user.general.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepository reviewRepository;
    private final ReviewMediaRepository reviewMediaRepository;
    private final ReviewHelpfulRepository reviewHelpfulRepository;
    private final AdminProductRepository adminProductRepository;
    private final AdminProductImageRepository adminProductImageRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final FileService fileService;

    @Override
    public ReviewResponseDto createReview(Long userId, ReviewRequestDto requestDto, List<MultipartFile> files) {

        // 1. 주문 아이템 조회
        OrderItemEntity orderItem = orderItemRepository.findById(requestDto.getOrderItemId())
                .orElseThrow(() -> new IllegalArgumentException("주문 아이템 없음"));

        // 2. 주문서와 상품 가져오기
        OrderEntity order = orderItem.getOrder();
        AdminProductEntity product = orderItem.getProduct();

        // 3. 유저 검증
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        // 4. 결제 완료 여부 검증
        boolean validOrder = orderRepository.existsByOrderIdAndUserIdAndOrderStatus(
                order.getOrderId(), userId, OrderStatus.PAID);
        if (!validOrder) {
            throw new IllegalStateException("결제 완료된 주문만 리뷰 작성이 가능합니다.");
        }

        // 5. 중복 리뷰 방지 (주문 아이템 단위로)
        if (reviewRepository.existsByOrderItemIdAndDeleteYn(requestDto.getOrderItemId(), "N")) {
            throw new IllegalStateException("이미 해당 주문 상품에 대한 리뷰가 존재합니다. 수정 또는 삭제만 가능합니다.");
        }

        // 6. 리뷰 저장
        ReviewEntity review = ReviewEntity.builder()
                .product(product)
                .user(user)
                .orderId(order.getOrderId())
                .orderItemId(orderItem.getOrderItemId())
                .rating(requestDto.getRating())
                .content(requestDto.getContent())
                .activeYn("Y")
                .deleteYn("N")
                .helpfulCount(0)
                .build();
        reviewRepository.save(review);

        // 7. 첨부파일 저장
        List<ReviewMediaEntity> medias = new ArrayList<>();
        if (files != null && !files.isEmpty()) {
            int sortOrder = 0;
            for (MultipartFile file : files) {
                if (file.isEmpty()) continue;

                String mediaType = file.getContentType() != null && file.getContentType().startsWith("video")
                        ? "VIDEO"
                        : "IMAGE";

                FileUploadResult result = fileService.save(file, "review/" + mediaType.toLowerCase());

                ReviewMediaEntity media = ReviewMediaEntity.builder()
                        .review(review)
                        .mediaType(mediaType)
                        .url(result.getUrlPath())
                        .sortOrder(sortOrder++)
                        .build();

                medias.add(media);
            }
            reviewMediaRepository.saveAll(medias);
        }

        // 8. DTO 변환 후 반환
        List<ReviewMediaDto> mediaDtos = medias.stream()
                .map(m -> ReviewMediaDto.builder()
                        .mediaType(m.getMediaType())
                        .url(m.getUrl())
                        .sortOrder(m.getSortOrder())
                        .build())
                .toList();

        return ReviewResponseDto.builder()
                .reviewId(review.getReviewId())
                .productId(product.getProductId())
                .rating(review.getRating())
                .content(review.getContent())
                .userName(user.getUserName())
                .createdAt(review.getCreatedAt())
                .medias(mediaDtos)
                .build();
    }


    @Override
    public ReviewResponseDto updateReview(Long userId, Long reviewId, ReviewUpdateRequestDto requestDto, List<MultipartFile> files) {
        ReviewEntity review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰 없음"));

        // 작성자 검증
        if (!review.getUser().getUserId().equals(userId)) {
            throw new IllegalStateException("본인 리뷰만 수정할 수 있습니다.");
        }

        // 본문 수정
        review.setRating(requestDto.getRating());
        review.setContent(requestDto.getContent());
        //review.setUpdatedAt(LocalDateTime.now()); DB에서 통제

        // 기존 미디어 중 삭제할 목록 처리
        if (requestDto.getDeleteMediaIds() != null && !requestDto.getDeleteMediaIds().isEmpty()) {
            List<ReviewMediaEntity> toDelete = reviewMediaRepository.findAllById(requestDto.getDeleteMediaIds());

            for (ReviewMediaEntity media : toDelete) {
                fileService.deleteQuietly(media.getUrl()); // 물리적 파일 삭제
            }

            reviewMediaRepository.deleteAll(toDelete);
        }

        // 새 파일 추가
        if (files != null && !files.isEmpty()) {
            int sortOrder = reviewMediaRepository.countByReview(review); // 기존 개수 기준으로 sort 이어붙이기
            List<ReviewMediaEntity> newMedias = new ArrayList<>();

            for (MultipartFile file : files) {
                if (file.isEmpty()) continue;

                String mediaType = file.getContentType() != null && file.getContentType().startsWith("video")
                        ? "VIDEO" : "IMAGE";
                FileUploadResult result = fileService.save(file, "review/" + mediaType.toLowerCase());

                ReviewMediaEntity media = ReviewMediaEntity.builder()
                        .review(review)
                        .mediaType(mediaType)
                        .url(result.getUrlPath())
                        .sortOrder(sortOrder++)
                        .build();
                newMedias.add(media);
            }

            reviewMediaRepository.saveAll(newMedias);
        }

        reviewRepository.save(review);

        List<ReviewMediaEntity> allMedias = reviewMediaRepository.findByReviewOrderByCreatedAtAsc(review);

        int i = 0;
        for (ReviewMediaEntity m : allMedias) {
            m.setSortOrder(i++);
        }
        reviewMediaRepository.saveAll(allMedias);

        // 최신 미디어 목록 조회
        List<ReviewMediaDto> mediaDtos = reviewMediaRepository.findByReview(review).stream()
                .map(m -> ReviewMediaDto.builder()
                        .mediaType(m.getMediaType())
                        .url(m.getUrl())
                        .sortOrder(m.getSortOrder())
                        .build())
                .toList();

        return ReviewResponseDto.builder()
                .reviewId(review.getReviewId())
                .productId(review.getProduct().getProductId())
                .rating(review.getRating())
                .content(review.getContent())
                .userName(review.getUser().getUserName())
                .createdAt(review.getCreatedAt())
                .medias(mediaDtos)
                .build();
    }

    @Override
    public void deleteReview(Long userId, Long reviewId) {
        ReviewEntity review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰 없음"));

        if (!review.getUser().getUserId().equals(userId)) {
            throw new IllegalStateException("본인 리뷰만 삭제할 수 있습니다.");
        }

        review.setDeleteYn("Y");
        reviewRepository.save(review);
    }

    @Override
    public Page<ReviewUserResponseDto> getMyReviews(Long userId, Pageable pageable) {
        Page<ReviewEntity> reviews = reviewRepository.findByUser_UserIdAndDeleteYn(userId, "N", pageable);

        return reviews.map(review -> {
            // 리뷰 첨부파일
            List<ReviewMediaDto> mediaDtos = reviewMediaRepository.findByReview(review).stream()
                    .map(m -> ReviewMediaDto.builder()
                            .mediaType(m.getMediaType())
                            .url(m.getUrl())
                            .sortOrder(m.getSortOrder())
                            .build())
                    .toList();

            // 상품 대표 이미지
            String productImageUrl = adminProductImageRepository
                    .findFirstByProduct_ProductIdOrderByImageSortOrderAsc(review.getProduct().getProductId())
                    .map(AdminProductImageEntity::getImageUrl)
                    .orElse(null);

            // 주문 아이템
            List<OrderItemEntity> orderItems = orderItemRepository.findByOrderIdAndProductId(
                    review.getOrderId(),
                    review.getProduct().getProductId()
            );

            List<ReviewOrderItemDto> orderItemDtos = orderItems.stream()
                    .map(orderItem -> ReviewOrderItemDto.builder()
                            .orderItemId(orderItem.getOrderItemId())
                            .options(orderItem.getOptions().stream()
                                    .map(opt -> OrderOptionResponseDto.builder()
                                            .optionId(opt.getProductOption().getOptionId())
                                            .optionValue(opt.getProductOption().getOptionValue())
                                            .additionalPrice(BigDecimal.valueOf(opt.getProductOption().getOptionAdditionalPrice()))
                                            .build())
                                    .toList())
                            .additionals(orderItem.getAdditionals().stream()
                                    .map(add -> OrderAdditionalResponseDto.builder()
                                            .additionalId(add.getProductAdditional().getAdditional().getAdditionalId())
                                            .additionalName(add.getProductAdditional().getAdditional().getAdditionalName())
                                            .additionalPrice(add.getAdditionalPrice())
                                            .quantity(add.getQuantity())
                                            .build())
                                    .toList())
                            .gifts(orderItem.getGifts().stream()
                                    .map(gift -> OrderGiftResponseDto.builder()
                                            .giftId(gift.getProductGift().getGift().getGiftId())
                                            .giftName(gift.getProductGift().getGift().getGiftName())
                                            .quantity(gift.getQuantity())
                                            .build())
                                    .toList())
                            .build()
                    )
                    .toList();

            return ReviewUserResponseDto.builder()
                    .reviewId(review.getReviewId())
                    .productId(review.getProduct().getProductId())
                    .productName(review.getProduct().getProductName())
                    .productImageUrl(productImageUrl)
                    .rating(review.getRating())
                    .helpfulCount(review.getHelpfulCount())
                    .content(review.getContent())
                    .userName(review.getUser().getUserName())
                    .createdAt(review.getCreatedAt())
                    .medias(mediaDtos)
                    .orderItems(orderItemDtos)
                    .build();
        });
    }

    @Override
    public Page<ReviewResponseDto> getReviewsByProduct(Long productId, String sortType, String filterType, Long currentUserId, Pageable pageable) {

        // 정렬 조건
        Sort sort;
        switch (sortType.toLowerCase()) {
            case "최신순":
                sort = Sort.by(Sort.Direction.DESC, "createdAt");
                break;
            case "평점 높은순":
                sort = Sort.by(Sort.Direction.DESC, "rating");
                break;
            case "평점 낮은순":
                sort = Sort.by(Sort.Direction.ASC, "rating");
                break;
            default: // 베스트순
                sort = Sort.by(Sort.Direction.DESC, "helpfulCount");
                break;
        }
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        // 필터 조건
        Page<ReviewEntity> reviews;
        switch (filterType.toLowerCase()) {
            case "포토상품평":
                reviews = reviewRepository.findPhotoReviews(productId, sortedPageable);
                break;
            case "동영상상품평":
                reviews = reviewRepository.findVideoReviews(productId, sortedPageable);
                break;
            case "일반상품평":
                reviews = reviewRepository.findTextOnlyReviews(productId, sortedPageable);
                break;
            default: // 전체
                reviews = reviewRepository.findByProduct_ProductIdAndDeleteYn(productId, "N", sortedPageable);
                break;
        }

        return reviews.map(review -> {
            List<ReviewMediaDto> mediaDtos = reviewMediaRepository.findByReview(review).stream()
                    .map(m -> ReviewMediaDto.builder()
                            .mediaType(m.getMediaType())
                            .url(m.getUrl())
                            .sortOrder(m.getSortOrder())
                            .build())
                    .toList();

            List<OrderItemEntity> orderItems = orderItemRepository.findByOrderIdAndProductId(
                    review.getOrderId(),
                    review.getProduct().getProductId()
            );

            List<ReviewOrderItemDto> orderItemDtos = orderItems.stream()
                    .map(orderItem -> ReviewOrderItemDto.builder()
                            .orderItemId(orderItem.getOrderItemId())
                            .options(orderItem.getOptions().stream()
                                    .map(opt -> OrderOptionResponseDto.builder()
                                            .optionId(opt.getProductOption().getOptionId())
                                            .optionValue(opt.getProductOption().getOptionValue())
                                            .additionalPrice(BigDecimal.valueOf(opt.getProductOption().getOptionAdditionalPrice()))
                                            .build())
                                    .toList())
                            .additionals(orderItem.getAdditionals().stream()
                                    .map(add -> OrderAdditionalResponseDto.builder()
                                            .additionalId(add.getProductAdditional().getAdditional().getAdditionalId())
                                            .additionalName(add.getProductAdditional().getAdditional().getAdditionalName())
                                            .additionalPrice(add.getAdditionalPrice())
                                            .quantity(add.getQuantity())
                                            .build())
                                    .toList())
                            .gifts(orderItem.getGifts().stream()
                                    .map(gift -> OrderGiftResponseDto.builder()
                                            .giftId(gift.getProductGift().getGift().getGiftId())
                                            .giftName(gift.getProductGift().getGift().getGiftName())
                                            .quantity(gift.getQuantity())
                                            .build())
                                    .toList())
                            .build()
                    )
                    .toList();

            boolean helpfulAdded = false;
            if (currentUserId != null) { // 로그인 사용자일 때만 체크
                helpfulAdded = reviewHelpfulRepository.existsByReview_ReviewIdAndUser_UserId(review.getReviewId(), currentUserId);
            }

            return ReviewResponseDto.builder()
                    .reviewId(review.getReviewId())
                    .productId(review.getProduct().getProductId())
                    .productName(review.getProduct().getProductName())
                    .rating(review.getRating())
                    .content(review.getContent())
                    .helpfulCount(review.getHelpfulCount())
                    .helpfulAdded(helpfulAdded)
                    .userName(review.getUser().getUserName())
                    .createdAt(review.getCreatedAt())
                    .medias(mediaDtos)
                    .orderItems(orderItemDtos)
                    .build();
        });
    }

    @Override
    @Transactional
    public boolean toggleHelpful(Long userId, Long reviewId) {
        ReviewEntity review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰가 존재하지 않습니다."));

        /*if (review.getUser().getUserId().equals(userId)) {
            throw new IllegalStateException("본인 리뷰에는 도움돼요를 누를 수 없습니다.");
        }*/

        Optional<ReviewHelpfulEntity> existing = reviewHelpfulRepository.findByReview_ReviewIdAndUser_UserId(reviewId, userId);

        if (existing.isPresent()) {
            // 이미 눌렀으면 → 취소
            reviewHelpfulRepository.delete(existing.get());
            review.setHelpfulCount(review.getHelpfulCount() - 1);
            reviewRepository.save(review);
            return false; // 취소됨
        } else {
            // 처음 누름 → 등록
            UserEntity user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

            ReviewHelpfulEntity helpful = ReviewHelpfulEntity.builder()
                    .review(review)
                    .user(user)
                    .build();

            reviewHelpfulRepository.save(helpful);

            review.setHelpfulCount(review.getHelpfulCount() + 1);
            reviewRepository.save(review);
            return true; // 추가됨
        }
    }

    @Override
    public ReviewSummaryDto getReviewSummary(Long productId) {
        Double avgRating = reviewRepository.findAverageRatingByProductId(productId);
        Long reviewCount = reviewRepository.countByProductIdAndDeleteYn(productId);

        return ReviewSummaryDto.builder()
                .productId(productId)
                .averageRating(avgRating != null ? Math.round(avgRating * 10) / 10.0 : 0.0)
                .reviewCount(reviewCount)
                .build();
    }

    public ReviewRatingStatsDto getReviewStats(Long productId) {
        List<Object[]> results = reviewRepository.countReviewsByRating(productId);

        Map<Integer, Long> ratingCounts = new HashMap<>();
        long total = 0;

        for (Object[] row : results) {
            Integer rating = (Integer) row[0];
            Long count = (Long) row[1];
            ratingCounts.put(rating, count);
            total += count;
        }

        Map<Integer, Integer> ratingPercents = new HashMap<>();
        if (total > 0) {
            for (Map.Entry<Integer, Long> entry : ratingCounts.entrySet()) {
                int percent = (int) Math.round((entry.getValue() * 100.0) / total);
                ratingPercents.put(entry.getKey(), percent);
            }
        }

        return ReviewRatingStatsDto.builder()
                .ratingCounts(ratingCounts)
                .ratingPercents(ratingPercents)
                .totalCount(total)
                .build();
    }


}
