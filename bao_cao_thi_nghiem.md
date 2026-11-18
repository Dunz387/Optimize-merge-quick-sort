# Báo cáo thí nghiệm: Tìm giá trị Cutoff tối ưu cho Merge Sort và Quick Sort

## 1. Mục tiêu thí nghiệm

**Cutoff** là ngưỡng kích thước mảng con mà tại đó thuật toán dừng đệ quy và chuyển sang Insertion Sort. Mục tiêu của thí nghiệm này là:

- Tìm giá trị cutoff cho **thời gian chạy ngắn nhất** trên máy cá nhân.
- Khảo sát ảnh hưởng của cutoff lên hiệu năng với **nhiều loại dữ liệu** và **nhiều kích thước** khác nhau.

---

## 2. Thuật toán sử dụng

| Thuật toán | Độ phức tạp trung bình | Độ phức tạp xấu nhất | Đặc điểm |
|---|---|---|---|
| **Merge Sort** | $O(N \log N)$ | $O(N \log N)$ | Ổn định, cần bộ nhớ phụ $O(N)$ |
| **Quick Sort** | $O(N \log N)$ | $O(N^2)$ | In-place, pivot = phần tử đầu |
| **Insertion Sort** | $O(N^2)$ | $O(N^2)$ | Hiệu quả trên mảng nhỏ hoặc gần có thứ tự |

---

## 3. Thiết kế thí nghiệm

### 3.1 Biến thí nghiệm

| Biến | Giá trị |
|---|---|
| Giá trị cutoff | 1, 2, 3, ..., 50 |
| Thời gian chạy | Đo bằng `System.nanoTime()` (nanosecond) |
| Thuật toán | `merge_sort`, `quick_sort` |
| Loại dữ liệu | random, sorted, reverse, partial |
| Kích thước mảng | 10,000 – 500,000 phần tử |

### 3.2 Dữ liệu thử nghiệm

7 bộ dữ liệu được chuẩn bị sẵn trong thư mục `data/`:

| File | Loại dữ liệu | Kích thước ($N$) | Mô tả |
|---|---|---|---|
| `random_10000.txt` | Ngẫu nhiên | 10,000 | Các số nguyên phân bố ngẫu nhiên |
| `random_50000.txt` | Ngẫu nhiên | 50,000 | Các số nguyên phân bố ngẫu nhiên |
| `random_100000.txt` | Ngẫu nhiên | 100,000 | Các số nguyên phân bố ngẫu nhiên |
| `random_500000.txt` | Ngẫu nhiên | 500,000 | Các số nguyên phân bố ngẫu nhiên |
| `sorted_10000.txt` | Đã sắp xếp tăng | 10,000 | Dãy số tăng dần |
| `reverse_10000.txt` | Sắp xếp giảm | 10,000 | Dãy số giảm dần |
| `partial_100000.txt` | Sắp xếp một phần | 100,000 | Một phần dữ liệu đã có thứ tự |


### 3.3 Quy trình đo

Với mỗi bộ **(thuật toán × dữ liệu × cutoff)**:


  Bước 1: Warm-up (3 lần chạy, không ghi kết quả)   

 Bước 2: Đo thực tế (5 lần chạy, ghi vào CSV)      
        → Mỗi lần clone mảng gốc, đo System.nanoTime



**Giải thích**:
- **Warm-up**: JVM sử dụng JIT (Just-In-Time) Compiler để biên dịch bytecode thành mã máy tối ưu khi phát hiện đoạn code được gọi nhiều lần ("hot code"). 3 vòng warm-up đảm bảo JIT đã tối ưu xong trước khi đo, giúp kết quả phản ánh đúng hiệu năng thực tế.
- **Clone mảng**: Mỗi lần đo sử dụng bản sao (`original.clone()`) của mảng gốc, đảm bảo mọi lần chạy đều bắt đầu từ cùng trạng thái dữ liệu.
- **5 lần đo**: Ghi lại cả 5 giá trị để sau đó tính **median** (trung vị)

### 3.4 Tham số thí nghiệm

| Tham số | Giá trị |
|---|---|
| Dải cutoff | 1 → 50 (bước nhảy: 1) |
| Số vòng warm-up | 3 |
| Số vòng đo thực tế | 5 |
| Đơn vị đo thời gian | Nanosecond (`System.nanoTime()`) |

### 3.5 Quy mô thí nghiệm

| Hạng mục | Số lượng |
|---|---|
| Số bộ dữ liệu | 7 |
| Số thuật toán | 2 |
| Số giá trị cutoff | 50 |
| Số lần đo / cấu hình | 5 |
| **Tổng số phép đo ghi vào CSV** | **3,500** |
| Số vòng warm-up (không ghi) | 2,100 |

---

## 4. Cách chạy thí nghiệm

### Yêu cầu
- **JDK 25** (hoặc tương thích) đã cài đặt
- Thư mục `data/` chứa đủ 7 file dữ liệu

### Thực hiện

```bash

# Bước 1: Biên dịch
javac Sorting_Algorithms.java CutoffExperiment.java

# Bước 2: Chạy thí nghiệm
java CutoffExperiment

# Bước 3: Phân tích kết quả
# Mở analize/cutoff_analysis.ipynb để biểu đồ biểu diễn kết quả.
```



---

## 6. Phân tích kết quả

Kết quả và biểu đồ phân tích nằm trong `analize/cutoff_analysis.ipynb`:

- **14 biểu đồ cột**: Mỗi biểu đồ dành riêng cho 1 thuật toán trên 1 bộ dữ liệu, thể hiện thời gian chạy (median) theo từng giá trị cutoff.
- **Bảng tổng hợp**: Liệt kê cutoff tối ưu kèm thời gian median cho tất cả cấu hình.
