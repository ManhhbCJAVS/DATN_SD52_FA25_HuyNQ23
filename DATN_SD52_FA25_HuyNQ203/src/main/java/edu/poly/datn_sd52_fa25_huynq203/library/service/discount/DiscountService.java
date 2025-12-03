//package edu.poly.datn_sd52_fa25_huynq203.library.service.discount;
//
//import edu.poly.datn_sd52_fa25_huynq203.admin.discount.payload.DiscountRequest;
//import edu.poly.datn_sd52_fa25_huynq203.admin.discount.payload.DiscountResponse;
//import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.discount.Discount;
//import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.discount.DiscountDetail;
//import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.variant.ProductVariant;
//import edu.poly.datn_sd52_fa25_huynq203.library.model.enums.DiscountStatus;
//import edu.poly.datn_sd52_fa25_huynq203.library.repository.discount.DiscountDetailRepository;
//import edu.poly.datn_sd52_fa25_huynq203.library.repository.discount.DiscountRepository;
//import edu.poly.datn_sd52_fa25_huynq203.library.repository.ProductVariantRepository;
//import edu.poly.datn_sd52_fa25_huynq203.library.service.discount.DiscountService;
//import lombok.RequiredArgsConstructor;
//import lombok.experimental.FieldDefaults;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
//@Slf4j
//public class DiscountService {
//
//
//    final DiscountRepository discountRepository;
//
//    final DiscountDetailRepository discountDetailRepository;
//
//    final ProductVariantRepository productVariantRepository;
//
//    static final String PREFIX = "DISC";
//    static int RANDOM_LENGTH = 4;
//    static String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
//
//    @Override
//    @Transactional
//    public List<DiscountResponse> getAll() {
//        List<Discount> discounts = discountRepository.findAll();
//        if (discounts.isEmpty()) {
//            throw new IllegalArgumentException("không có đợt giảm giá nào" + discounts);
//        }
//
//        return discounts.stream()
//                .sorted(Comparator.comparing(Discount::getId).reversed()) // Sắp xếp theo id giảm dần
//                .map(this::mapToResponse)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    @Transactional
//    public DiscountResponse findById(Long id) {
//        Discount discount = discountRepository.findById(id).orElseThrow(() ->
//                new IllegalArgumentException("không tim thấy phiếu giảm gía" + id));
//        return mapToResponse(discount);
//    }
//
//    @Override
//    @Transactional
//    public void updateDiscount(DiscountRequest request, Long id) {
//        Discount discount = discountRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đợt giảm giá với ID: " + id));
//
//        validateDiscountRequest(request);
//
//        // Chỉ cho phép chỉnh sửa nếu đang UPCOMING
//        if (discount.getStatus() != DiscountStatus.UPCOMING) {
//            throw new IllegalStateException("Chỉ có thể chỉnh sửa khi đợt giảm giá ở trạng thái UPCOMING");
//        }
//
//        // Cập nhật thông tin cơ bản
//        if (request.getName() != null && !request.getName().isBlank()) {
//            discount.setName(request.getName());
//        }
//        if (request.getDiscountPercentage() != null) {
//            discount.setDiscountPercentage(request.getDiscountPercentage());
//        }
//        if (request.getStartDate() != null) {
//            discount.setStartDate(request.getStartDate());
//        }
//        if (request.getEndDate() != null) {
//            discount.setEndDate(request.getEndDate());
//        }
//
//        // Cập nhật danh sách sản phẩm
//        if (request.getProductVariantIds() != null) {
//            List<ProductVariant> newVariants = productVariantRepository.findAllById(request.getProductVariantIds());
//            if (newVariants.isEmpty()) {
//                throw new IllegalArgumentException("Không tìm thấy sản phẩm hợp lệ để áp dụng giảm giá");
//            }
//
//            // Load danh sách hiện tại (tránh lazy)
//            List<DiscountDetail> currentDetails = new ArrayList<>(discount.getDiscountDetails());
//
//            // Xóa những DiscountDetail không còn trong danh sách mới
//            for (DiscountDetail detail : currentDetails) {
//                if (!newVariants.contains(detail.getProductVariant())) {
//                    discount.getDiscountDetails().remove(detail); // orphanRemoval = true sẽ tự delete
//                }
//            }
//
//            // Thêm mới những sản phẩm chưa có
//            for (ProductVariant variant : newVariants) {
//                boolean exists = currentDetails.stream()
//                        .anyMatch(detail -> detail.getProductVariant().getId().equals(variant.getId()));
//                if (!exists) {
//                    DiscountDetail newDetail = DiscountDetail.builder()
//                            .discount(discount)
//                            .productVariant(variant)
//                            .deleted(false)
//                            .build();
//                    discount.getDiscountDetails().add(newDetail);
//                }
//            }
//        }
//
//        discount.setUpdatedAt(LocalDateTime.now());
//        discountRepository.save(discount);
//
//        updateProductDiscountPriority(LocalDateTime.now());
//
//    }
//
//
//    @Override
//    @Transactional
//    public List<DiscountResponse> findByName(String name) {
//        List<Discount> discounts = discountRepository.findByNameContainingIgnoreCase(name);
//        if (discounts.isEmpty()) {
//            throw new IllegalArgumentException("không tìm thấy");
//        }
//
//        return discounts.stream().map(this::mapToResponse).collect(Collectors.toList());
//    }
//
//    @Override
//    @Transactional
//    public List<DiscountResponse> findByStatus(DiscountStatus status) {
//        List<Discount> discounts = discountRepository.findByStatus(status);
//        return discounts.stream().map(this::mapToResponse).collect(Collectors.toList());
//    }
//
//
//    @Override
//    @Transactional
//    public DiscountResponse create(DiscountRequest discountRequest) {
//        validateDiscountRequest(discountRequest);
//
//        LocalDateTime now = LocalDateTime.now();
//
//        DiscountStatus status;
//        if (discountRequest.getEndDate().isBefore(now)) {
//            status = DiscountStatus.EXPIRED;
//        } else if (discountRequest.getStartDate().isAfter(now)) {
//            status = DiscountStatus.UPCOMING;
//        } else {
//            status = DiscountStatus.ACTIVE;
//        }
//
//        // 1️⃣ Tạo mới discount
//        Discount discount = Discount.builder()
//                .name(discountRequest.getName())
//                .code(generateDiscountCode())
//                .discountPercentage(discountRequest.getDiscountPercentage())
//                .startDate(discountRequest.getStartDate())
//                .endDate(discountRequest.getEndDate())
//                .status(status)
//                .createdAt(now)
//                .updatedAt(now)
//                .build();
//
//        discountRepository.save(discount);
//
//        //  Lấy danh sách ProductVariant được chọn
//        List<ProductVariant> variants = productVariantRepository.findAllById(discountRequest.getProductVariantIds());
//        if (variants.isEmpty()) {
//            throw new IllegalArgumentException("Không tìm thấy sản phẩm áp dụng giảm giá.");
//        }
//
//        // Tạo DiscountDetail cho từng sản phẩm
//        List<DiscountDetail> discountDetails = new ArrayList<>();
//        for (ProductVariant variant : variants) {
//            // Kiểm tra các discount khác đang ACTIVE cho sản phẩm này
//            List<DiscountDetail> existingDetails = discountDetailRepository.findActiveDiscountsForVariant(
//                    variant.getId(), now);
//
//            // Tìm discount hiện tại có phần trăm lớn nhất
//            Optional<DiscountDetail> bestDiscount = existingDetails.stream()
//                    .filter(d -> d.getDiscount().getStartDate().isBefore(discountRequest.getEndDate())
//                            && d.getDiscount().getEndDate().isAfter(discountRequest.getStartDate()))
//                    .max(Comparator.comparingDouble((DiscountDetail d) -> d.getDiscount().getDiscountPercentage())
//                            .thenComparing(d -> d.getDiscount().getCreatedAt()));
//
//            // Nếu discount mới có phần trăm lớn hơn hoặc mới hơn, thì áp dụng
//            boolean shouldApplyNewDiscount = bestDiscount.isEmpty()
//                    || discountRequest.getDiscountPercentage() > bestDiscount.get().getDiscount().getDiscountPercentage()
//                    || (Objects.equals(discountRequest.getDiscountPercentage(),
//                    bestDiscount.get().getDiscount().getDiscountPercentage())
//                    && now.isAfter(bestDiscount.get().getDiscount().getCreatedAt()));
//
//            if (shouldApplyNewDiscount) {
//                DiscountDetail detail = DiscountDetail.builder()
//                        .productVariant(variant)
//                        .discount(discount)
//                        .build();
//                discountDetails.add(detail);
//
//                if (status == DiscountStatus.ACTIVE) {
//                    double originalPrice = variant.getPrice();
//                    double discountPercent = discountRequest.getDiscountPercentage();
//                    double finalPrice = originalPrice - (originalPrice * discountPercent / 100);
//                    variant.setFinalPrice(finalPrice);
//                    productVariantRepository.save(variant);
//                }
//
//            }
//        }
//
////        if (discountDetails.isEmpty()) {
////            throw new IllegalStateException("Không có sản phẩm nào được áp dụng đợt giảm giá này (đã có giảm giá cao hơn).");
////        }
//
//        discountDetailRepository.saveAll(discountDetails);
//        discount.setDiscountDetails(discountDetails);
//
//        return mapToResponse(discount);
//    }
//
//
//    @Scheduled(cron = "*/30 * * * * *") // sau 30s
//    @Transactional
//    public void updateDiscountStatuses() {
//        LocalDateTime now = LocalDateTime.now();
//
//        List<Discount> allDiscounts = discountRepository.findAll();
//        for (Discount discount : allDiscounts) {
//            DiscountStatus currentStatus = discount.getStatus();
//            DiscountStatus newStatus = currentStatus;
//
//            if (discount.getEndDate().isBefore(now)) {
//                newStatus = DiscountStatus.EXPIRED;
//            } else if (discount.getStartDate().isAfter(now)) {
//                newStatus = DiscountStatus.UPCOMING;
//            } else if (discount.getStartDate().isBefore(now) && discount.getEndDate().isAfter(now)) {
//                newStatus = DiscountStatus.ACTIVE;
//            }
//
//            if (newStatus != currentStatus) {
//                discount.setStatus(newStatus);
//                discount.setUpdatedAt(now);
//                discountRepository.save(discount);
//
//                //  Nếu chuyển sang ACTIVE -> cập nhật giá giảm cho sản phẩm
//                if (newStatus == DiscountStatus.ACTIVE) {
//                    applyDiscountToVariants(discount);
//                }
//
//                //  Nếu chuyển sang EXPIRED -> hoàn giá gốc cho sản phẩm
//                if (newStatus == DiscountStatus.EXPIRED) {
//                    revertDiscountForVariants(discount);
//                }
//            }
//        }
//
//        updateProductDiscountPriority(now);
//    }
//
//
//    private void applyDiscountToVariants(Discount discount) {
//        List<DiscountDetail> details = discountDetailRepository.findByDiscountId(discount.getId());
//        for (DiscountDetail detail : details) {
//            ProductVariant variant = detail.getProductVariant();
//
//            // 🔍 Kiểm tra xem có discount nào khác đang ACTIVE cho cùng variant không
//            List<DiscountDetail> activeDetails = discountDetailRepository.findActiveDiscountsForVariant(variant.getId(), LocalDateTime.now());
//            DiscountDetail highest = activeDetails.stream()
//                    .max(Comparator.comparingDouble(d -> d.getDiscount().getDiscountPercentage()))
//                    .orElse(detail); // nếu chỉ có chính discount này thì dùng luôn
//
//            // ✅ Chỉ giảm nếu đây là discount cao nhất
//            if (Objects.equals(highest.getDiscount().getId(), discount.getId())) {
//                double originalPrice = variant.getPrice();
//                double discountPercent = discount.getDiscountPercentage();
//                double finalPrice = originalPrice - (originalPrice * discountPercent / 100);
//                variant.setFinalPrice(finalPrice);
//                productVariantRepository.save(variant);
//            }
//        }
//    }
//
//
//    private void revertDiscountForVariants(Discount discount) {
//        List<DiscountDetail> details = discountDetailRepository.findByDiscountId(discount.getId());
//        LocalDateTime now = LocalDateTime.now();
//
//        for (DiscountDetail detail : details) {
//            ProductVariant variant = detail.getProductVariant();
//
//            // Kiểm tra xem variant này có discount khác đang ACTIVE không
//            List<DiscountDetail> activeDetails = discountDetailRepository.findActiveDiscountsForVariant(variant.getId(), now)
//                    .stream()
//                    .filter(d -> !Objects.equals(d.getDiscount().getId(), discount.getId()))
//                    .toList();
//
//            if (activeDetails.isEmpty()) {
//                // ✅ Không còn discount nào → quay lại giá gốc
//                variant.setFinalPrice(variant.getPrice());
//            } else {
//                // ✅ Còn discount khác → áp dụng discount cao nhất
//                DiscountDetail best = activeDetails.stream()
//                        .max(Comparator.comparingDouble(d -> d.getDiscount().getDiscountPercentage()))
//                        .get();
//                double finalPrice = variant.getPrice() - (variant.getPrice() * best.getDiscount().getDiscountPercentage() / 100);
//                variant.setFinalPrice(finalPrice);
//            }
//            productVariantRepository.save(variant);
//        }
//    }
//
//
//    /**
//     * Đảm bảo mỗi sản phẩm chỉ có 1 đợt giảm giá hoạt động (cao nhất) tại một thời điểm.
//     * Khi discount hiện tại hết hạn -> tự động kích hoạt discount kế tiếp (nếu có),
//     * và cập nhật lại giá sản phẩm tương ứng.
//     */
////    private void updateProductDiscountPriority(LocalDateTime now) {
////        // 🔹 1. Lấy tất cả DiscountDetail còn hiệu lực (ACTIVE hoặc UPCOMING)
////        List<DiscountDetail> validDetails = discountDetailRepository.findAll()
////                .stream()
////                .filter(d -> {
////                    Discount discount = d.getDiscount();
////                    return discount.getStatus() == DiscountStatus.ACTIVE
////                            || discount.getStatus() == DiscountStatus.UPCOMING;
////                })
////                .collect(Collectors.toList());
////
////        // 🔹 2. Gom nhóm theo ProductVariant
////        Map<Long, List<DiscountDetail>> grouped = validDetails.stream()
////                .collect(Collectors.groupingBy(d -> d.getProductVariant().getId()));
////
////        // 🔹 3. Duyệt qua từng nhóm sản phẩm
////        for (Map.Entry<Long, List<DiscountDetail>> entry : grouped.entrySet()) {
////            List<DiscountDetail> discounts = entry.getValue();
////
////            // ✅ 3.1 Tìm discount ACTIVE có % cao nhất
////            Optional<DiscountDetail> highestActive = discounts.stream()
////                    .filter(d -> d.getDiscount().getStatus() == DiscountStatus.ACTIVE)
////                    .max(Comparator.comparingDouble(d -> d.getDiscount().getDiscountPercentage()));
////
////            // ⚙️ 3.2 Nếu không có discount ACTIVE -> kích hoạt discount UPCOMING gần nhất
////            if (highestActive.isEmpty()) {
////                Optional<DiscountDetail> nextUpcoming = discounts.stream()
////                        .filter(d -> d.getDiscount().getStatus() == DiscountStatus.UPCOMING
////                                && d.getDiscount().getStartDate().isBefore(now.plusDays(1)))
////                        .min(Comparator.comparing(d -> d.getDiscount().getStartDate()));
////
////                nextUpcoming.ifPresent(detail -> {
////                    Discount nextDiscount = detail.getDiscount();
////                    nextDiscount.setStatus(DiscountStatus.ACTIVE);
////                    discountRepository.save(nextDiscount);
////
////                    // 🔥 Áp lại giá cho sản phẩm ngay sau khi kích hoạt discount kế tiếp
////                    applyDiscountToVariants(nextDiscount);
////                });
////            } else {
////                DiscountDetail activeDetail = highestActive.get();
////                Discount activeDiscount = activeDetail.getDiscount();
////
////                // ⚠️ 3.3 Nếu discount hiện tại đã hết hạn -> chuyển sang EXPIRED
////                if (activeDiscount.getEndDate().isBefore(now)) {
////                    activeDiscount.setStatus(DiscountStatus.EXPIRED);
////                    discountRepository.save(activeDiscount);
////
////                    // 🔄 3.4 Tìm discount UPCOMING kế tiếp để kích hoạt
////                    discounts.stream()
////                            .filter(d -> d.getDiscount().getStatus() == DiscountStatus.UPCOMING)
////                            .min(Comparator.comparing(d -> d.getDiscount().getStartDate()))
////                            .ifPresent(next -> {
////                                Discount nextDiscount = next.getDiscount();
////                                nextDiscount.setStatus(DiscountStatus.ACTIVE);
////                                discountRepository.save(nextDiscount);
////
////                                // 🔥 Áp lại giá giảm cho sản phẩm sau khi discount kế tiếp kích hoạt
////                                applyDiscountToVariants(nextDiscount);
////                            });
////                }
////            }
////        }
////    }
//
//
//    /**
//     * Kiểm tra và đảm bảo mỗi sản phẩm chỉ có 1 đợt giảm giá "hoạt động" (cao nhất)
//     * cũng được hjhj
//     */
//
////    private void updateProductDiscountPriority(LocalDateTime now) {
////        // Lấy tất cả chi tiết giảm giá còn hiệu lực
////        List<DiscountDetail> validDetails = discountDetailRepository.findAll()
////                .stream()
////                .filter(d -> {
////                    Discount discount = d.getDiscount();
////                    return discount.getStatus() == DiscountStatus.ACTIVE
////                            || discount.getStatus() == DiscountStatus.UPCOMING;
////                })
////                .collect(Collectors.toList());
////
////        // Gom theo productVariantId
////        Map<Long, List<DiscountDetail>> grouped = validDetails.stream()
////                .collect(Collectors.groupingBy(d -> d.getProductVariant().getId()));
////
////        for (Map.Entry<Long, List<DiscountDetail>> entry : grouped.entrySet()) {
////            List<DiscountDetail> discounts = entry.getValue();
////
////
////            Optional<DiscountDetail> highest = discounts.stream()
////                    .filter(d -> d.getDiscount().getStatus() == DiscountStatus.ACTIVE)
////                    .max(Comparator.comparingDouble(d -> d.getDiscount().getDiscountPercentage()));
////
////            // Nếu không có discount ACTIVE nào, thử tìm UPCOMING sắp diễn ra
////            if (highest.isEmpty()) {
////                Optional<DiscountDetail> next = discounts.stream()
////                        .filter(d -> d.getDiscount().getStatus() == DiscountStatus.UPCOMING
////                                && d.getDiscount().getStartDate().isBefore(now.plusDays(1)))
////                        .min(Comparator.comparing(d -> d.getDiscount().getStartDate()));
////
////                next.ifPresent(detail -> {
////                    detail.getDiscount().setStatus(DiscountStatus.ACTIVE);
////                    discountRepository.save(detail.getDiscount());
////                });
////            } else {
////                DiscountDetail active = highest.get();
////
////                // Hết hạn thì chuyển sang đợt tiếp theo nếu có
////                if (active.getDiscount().getEndDate().isBefore(now)) {
////                    active.getDiscount().setStatus(DiscountStatus.EXPIRED);
////                    discountRepository.save(active.getDiscount());
////
////                    // Kích hoạt đợt tiếp theo nếu có
////                    discounts.stream()
////                            .filter(d -> d.getDiscount().getStatus() == DiscountStatus.UPCOMING)
////                            .min(Comparator.comparing(d -> d.getDiscount().getStartDate()))
////                            .ifPresent(next -> {
////                                next.getDiscount().setStatus(DiscountStatus.ACTIVE);
////                                discountRepository.save(next.getDiscount());
////                            });
////                }
////            }
////        }
////    }
//
//
////    cái này phân tích đang hợp lý hơn so với mọi cái //hàm V2 tính tiếp
//    private void updateProductDiscountPriority(LocalDateTime now) {
//
//        List<DiscountDetail> validDetails = discountDetailRepository.findAll()
//                .stream()
//                .filter(d -> {
//                    Discount dis = d.getDiscount();
//                    return !dis.getEndDate().isBefore(now);
//                })
//                .collect(Collectors.toList());
//
//        Map<Long, List<DiscountDetail>> grouped = validDetails.stream()
//                .collect(Collectors.groupingBy(d -> d.getProductVariant().getId()));
//
//
//        for (Map.Entry<Long, List<DiscountDetail>> entry : grouped.entrySet()) {
//            Long variantId = entry.getKey();
//            List<DiscountDetail> discounts = entry.getValue();
//
//            // Lọc các discount hiện đang trong thời gian hiệu lực (bắt đầu <= now <= kết thúc)
//            List<DiscountDetail> activeRange = discounts.stream()
//                    .filter(d -> !d.getDiscount().getStartDate().isAfter(now)
//                            && !d.getDiscount().getEndDate().isBefore(now))
//                    .toList();
//
//
//            if (activeRange.isEmpty()) continue;
//
//
//            DiscountDetail highest = activeRange.stream()
//                    .max(Comparator.comparingDouble((DiscountDetail d) -> d.getDiscount().getDiscountPercentage())
//                            .thenComparing(d -> d.getDiscount().getCreatedAt()))
//                    .get();
//
//            Discount highestDiscount = highest.getDiscount();
//
//
//            if (highestDiscount.getStatus() != DiscountStatus.ACTIVE) {
//                highestDiscount.setStatus(DiscountStatus.ACTIVE);
//                discountRepository.save(highestDiscount);
//                applyDiscountToVariants(highestDiscount);
//            }
//
//
//            for (DiscountDetail other : activeRange) {
//                Discount d = other.getDiscount();
//                if (!Objects.equals(d.getId(), highestDiscount.getId())
//                        && d.getStatus() == DiscountStatus.ACTIVE) {
//                    d.setStatus(DiscountStatus.INACTIVE);
//                    discountRepository.save(d);
//                }
//            }
//
//            discounts.stream()
//                    .filter(d -> d.getDiscount().getEndDate().isBefore(now)
//                            && d.getDiscount().getStatus() != DiscountStatus.EXPIRED)
//                    .forEach(d -> {
//                        Discount expired = d.getDiscount();
//                        expired.setStatus(DiscountStatus.EXPIRED);
//                        discountRepository.save(expired);
//                        revertDiscountForVariants(expired);
//                    });
//
//
//            if (highestDiscount.getEndDate().isBefore(now)) {
//                highestDiscount.setStatus(DiscountStatus.EXPIRED);
//                discountRepository.save(highestDiscount);
//                revertDiscountForVariants(highestDiscount);
//
//                discounts.stream()
//                        .filter(d -> d.getDiscount().getStatus() == DiscountStatus.UPCOMING
//                                && !d.getDiscount().getStartDate().isAfter(now))
//                        .max(Comparator.comparingDouble(d -> d.getDiscount().getDiscountPercentage()))
//                        .ifPresent(next -> {
//                            Discount nextDiscount = next.getDiscount();
//                            nextDiscount.setStatus(DiscountStatus.ACTIVE);
//                            discountRepository.save(nextDiscount);
//                            applyDiscountToVariants(nextDiscount);
//                        });
//            }
//        }
//    }
//
//
//    private void validateDiscountRequest(DiscountRequest request) {
//        LocalDateTime now = LocalDateTime.now();
//
//        if (request.getStartDate() == null || request.getEndDate() == null) {
//            throw new IllegalArgumentException("Ngày bắt đầu và kết thúc không được để trống");
//        }
//
//        if (request.getEndDate().isBefore(request.getStartDate())) {
//            throw new IllegalArgumentException("Ngày kết thúc phải sau ngày bắt đầu");
//        }
//
//        if (request.getEndDate().isBefore(now)) {
//            throw new IllegalArgumentException("Không thể tạo đợt giảm giá đã kết thúc trong quá khứ");
//        }
//
//        if (request.getDiscountPercentage() == null
//                || request.getDiscountPercentage() <= 0
//                || request.getDiscountPercentage() > 100) {
//            throw new IllegalArgumentException("Phần trăm giảm giá phải nằm trong khoảng 0–100");
//        }
//
//        if (request.getProductVariantIds() == null || request.getProductVariantIds().isEmpty()) {
//            throw new IllegalArgumentException("Danh sách sản phẩm áp dụng không được để trống");
//        }
//
//        if (discountRepository.existsByName(request.getName())) {
//            throw new IllegalArgumentException("Tên đợt giảm giá đã tồn tại");
//        }
//
//    }
//
//    private DiscountResponse mapToResponse(Discount discount) {
//        if (discount == null) return null;
//
//        return DiscountResponse.builder()
//                .id(discount.getId())
//                .name(discount.getName())
//                .discountPercentage(discount.getDiscountPercentage())
////                .priceThreshold(discount.getPriceThreshold())
//                .status(discount.getStatus() != null ? discount.getStatus().name() : null)
//                .startDate(discount.getStartDate())
//                .endDate(discount.getEndDate())
//                .productVariants(
//                        discount.getDiscountDetails() != null
//                                ? discount.getDiscountDetails().stream()
//                                .filter(detail -> detail.getProductVariant() != null)
//                                .map(detail -> {
//                                    var variant = detail.getProductVariant();
//                                    return DiscountResponse.ProductVariant.builder()
//                                            .id(variant.getId())
//                                            .name(variant.getProduct().getName())
//                                            .originalPrice(variant.getPrice()) // giá lúc đầu
//                                            .finalPrice(variant.getFinalPrice())  // giá sau khi giảm
//                                            .build();
//                                })
//                                .toList()
//                                : List.of()
//                )
//                .build();
//    }
//
//    private static String generateDiscountCode() {
//        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
//        String random = generateRandomString(RANDOM_LENGTH);
//        return PREFIX + "-" + date + "-" + random;
//    }
//
//    private static String generateRandomString(int length) {
//        Random random = new Random();
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < length; i++) {
//            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
//        }
//        return sb.toString();
//    }
//
//}
