package edu.poly.datn_sd52_fa25_huynq203.library.untils;

public interface AppConst {
    String SEARCH_OPERATOR = "(\\w+?)(:|<|>)(.*)";

    /**
     * Chia 1 chuỗi thành 5 nhóm:
     * (\\w+?)        => Nhóm 1: Tìm một/nhiều ký tự chữ (a-z, A-Z, 0-9, _)
     *
     * ([<:>~!])      => Nhóm 2: (character class) : khớp 1 trong các ký tự <, :, >, ~, !
     *
     * (.*)           => Nhóm 3: . = bất kỳ ký tự nào & * = lặp lại nhiều lần (Nguyên lý Greedy Quantifier)
     *                => Ăn hết phần còn lại.
     *
     * (\\p{Punct}?)  => Nhóm 4: Tìm một ký tự dấu câu (punctuation) xuất hiện 0 hoặc 1 lần
     *                \p{Punct} = ký tự dấu câu (punctuation), như ., ,, ", ', ;, (, ).
     *                ? = xuất hiện 0/1 lần.
     *                => Nhóm 5: G.trị <=> kết thúc bằng 2 dấu câu liên tiếp.
     */
//    String SEARCH_SPEC_OPERATOR = "(\\w+?)([<:>~!])([\\p{Punct}]?)(.*?)([\\p{Punct}]?)$";

    /**
     * Sửa đổi ở Nhóm 1:
     * TỪ: (\\w+?)
     * THÀNH: ([\\w\\.]+?)
     * => [\\w\\.] nghĩa là "khớp bất kỳ ký tự nào là \w HOẶC dấu chấm \."
     *
     * (([\\w\\.]+?)) => Nhóm 1: Tìm key (cho phép dấu chấm, ví dụ: "brand.id")
     * ([<:>~!])       => Nhóm 2: operator
     * ([\\p{Punct}]?) => Nhóm 3: prefix
     * (.*?)           => Nhóm 4: value
     * ([\\p{Punct}]?)$ => Nhóm 5: suffix
     */
    String SEARCH_SPEC_OPERATOR = "([\\w\\.]+?)([<:>~!])([\\p{Punct}]?)(.*?)([\\p{Punct}]?)$";

    String SORT_BY = "(\\w+?)(:)(.*)";
}
