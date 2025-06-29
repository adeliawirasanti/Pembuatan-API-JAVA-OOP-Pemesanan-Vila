# **API JAVA OOP | SISTEM PEMESANAN VILA**  

Nama: Ni Made Adelia Wirasanti  
NIM: 2405551010  
Kelas: PBO B  

Nama: Ni Putu Candradevi Davantari  
NIM: 2405551035  
Kelas: PBO B  

Nama: Kharisma Rosary Fitri Sanda  
NIM: 2405551072  
Kelas: PBO B  

Nama: Ni Luh Putu Indah Suari  
NIM: 2405551103  
Kelas: PBO B  


## **INTRODUCING** 
Tugas ini merupakan proyek pembuatan backend API sederhana yang digunakan sebagai pemenuhan tugas akhir dari mata kuliah Pemrograman Berorientasi Obyek dan dikembangkan menggunakan bahasa pemrograman Java dengan pendekatan Pemrograman Berorientasi Objek (OOP). Proyek ini dirancang untuk membangun sistem pemesanan vila yang mencakup berbagai kebutuhan seperti manajemen data vila, tipe kamar, pemesanan, ulasan pelanggan, serta penggunaan voucher diskon. API ini menerapkan konsep RESTful, yang berarti seluruh komunikasi antar client dan server dilakukan dengan menggunakan HTTP method seperti GET, POST, PUT, dan DELETE. Semua data yang dikirim dan diterima dari server menggunakan format JSON, dan data disimpan secara lokal dalam basis data SQLite. Untuk melakukan pengujian terhadap seluruh endpoint API, aplikasi Postman digunakan sebagai alat bantu. Sistem ini juga dilengkapi dengan autentikasi melalui API key yang disisipkan secara hardcoded dalam program untuk membatasi akses


## **STRUCTURE**  
Struktur program pada proyek ini dibuat secara modular dengan prinsip Object-Oriented Programming (OOP) agar kode rapi, mudah dipelihara, dan dikembangkan. Seluruh kode sumber disimpan dalam folder src dan dibagi ke dalam beberapa package sesuai fungsinya, yang juga mencerminkan alur kerja program: permintaan dari pengguna diproses oleh controller, diteruskan ke query untuk mengakses data, hasilnya dikemas dalam model, lalu dikembalikan sebagai response.
* **Package models:**  Berisi class-class yang merepresentasikan entitas utama seperti Booking, Customer, Review, Room, Villa, dan Voucher. Setiap class model ini mencerminkan struktur tabel yang ada di database SQLite.
* **Package controllers:** Mengatur logika bisnis utama, seperti mengambil data dari database, memvalidasi input dari pengguna, serta memproses atau memanipulasi data berdasarkan kebutuhan aplikasi.
* **Package routes:** Menyediakan berbagai endpoint HTTP (seperti GET, POST, PUT, DELETE) yang bisa diakses pengguna. Package ini juga bertugas mengarahkan request ke controller yang sesuai.
* **Package queries:** Menyimpan query-query SQL yang digunakan untuk berinteraksi dengan database. Pemisahan ini bertujuan agar logika bisnis dan logika SQL tidak tercampur, sehingga lebih mudah dibaca dan dikelola.
* **Package database:** Mengatur koneksi ke database SQLite, termasuk proses inisialisasi dan pengelolaan siklus hidup database selama aplikasi berjalan.
* **Package exceptions:** Berisi class-class untuk menangani berbagai jenis error atau kondisi khusus, seperti data yang tidak ditemukan, input yang tidak valid, atau permintaan tanpa izin akses.
* **Package utils:** Menyediakan berbagai fungsi bantu, seperti validasi format data, parsing JSON, hingga pengecekan email atau tanggal, yang digunakan di berbagai bagian program.
* **Package core:** Di sinilah server HTTP dijalankan. API key untuk autentikasi disetting secara hardcoded, dan server mulai menerima serta memproses permintaan dari pengguna.


## **ERROR HANDLING**
Program aplikasi dalam proyek ini dilengkapi dengan sistem penanganan error yang menggunakan konsep Exception dalam Java. Jika terjadi kesalahan selama pemrosesan request, server akan memberikan response berupa status HTTP dan pesan error dalam format JSON. Beberapa contoh penanganan error yang diterapkan antara lain: 
* Jika pengguna mengakses data dengan ID yang tidak tersedia, maka sistem akan mengembalikan HTTP 404 Not Found.
* Jika pengguna mengirim data yang tidak lengkap saat membuat entitas baru (seperti customer tanpa email atau booking tanpa tanggal), maka sistem akan mengembalikan HTTP 400 Bad Request dengan penjelasan error.
* Jika format data tidak sesuai, misalnya email tidak valid atau kapasitas kamar bukan angka, maka juga akan dikembalikan HTTP 400 Bad Request.
* Jika pengguna tidak menyertakan API key, atau API key tidak sesuai, maka sistem akan mengembalikan HTTP 401.


## **TEST IN POSTMAN**
Saat program aplikasi dalam proyek ini dijalankan, server API akan aktif di localhost:8080. Meskipun dapat diakses lewat browser, pengujian dilakukan menggunakan Postman agar semua metode request seperti GET, POST, PUT, dan DELETE bisa dicoba dengan mudah.

# 📚 Villa Booking API Documentation

## Endpoints: Villas

### GET
- **`GET /villas`**  
  Daftar semua vila  
  ![/villas](https://github.com/adeliawirasanti/Pembuatan-API-JAVA-OOP-Pemesanan-Vila/raw/main/images/villas.png)

- **`GET /villas/{id}`**  
  Informasi detail suatu vila  
  ![GET Villa by ID](images/get-villa-id.png)

- **`GET /villas/{id}/rooms`**  
  Informasi kamar suatu vila, lengkap dengan fasilitas dan harga  
  ![GET Villa Rooms](images/get-villa-rooms.png)

- **`GET /villas/{id}/bookings`**  
  Daftar semua booking pada suatu vila  
  ![GET Villa Bookings](images/get-villa-bookings.png)

- **`GET /villas/{id}/reviews`**  
  Daftar semua review pada suatu vila  
  ![GET Villa Reviews](images/get-villa-reviews.png)

- **`GET /villas?ci_date={checkin_date}&co_date={checkout_date}`**  
  Pencarian ketersediaan vila berdasarkan tanggal check-in dan checkout  
  ![GET Availability](images/get-availability.png)

### POST
- **`POST /villas`**  
  Menambahkan data vila  
  ![POST Villa](images/post-villa.png)

- **`POST /villas/{id}/rooms`**  
  Menambahkan tipe kamar pada vila  
  ![POST Room](images/post-room.png)

### PUT
- **`PUT /villas/{id}`**  
  Mengubah data suatu vila  
  ![PUT Villa](images/put-villa.png)

- **`PUT /villas/{id}/rooms/{id}`**  
  Mengubah informasi kamar suatu vila  
  ![PUT Room](images/put-room.png)

### DELETE
- **`DELETE /villas/{id}/rooms/{id}`**  
  Menghapus kamar suatu vila  
  ![DELETE Room](images/delete-room.png)

- **`DELETE /villas/{id}`**  
  Menghapus data suatu vila  
  ![DELETE Villa](images/delete-villa.png)

---

## Endpoints: Customers

### GET
- **`GET /customers`**  
  Daftar semua customer  
  ![GET Customers](images/get-customers.png)

- **`GET /customers/{id}`**  
  Informasi detail seorang customer  
  ![GET Customer ID](images/get-customer-id.png)

- **`GET /customers/{id}/bookings`**  
  Daftar booking yang telah dilakukan  
  ![GET Customer Bookings](images/get-customer-bookings.png)

- **`GET /customers/{id}/reviews`**  
  Daftar ulasan dari customer  
  ![GET Customer Reviews](images/get-customer-reviews.png)

### POST
- **`POST /customers`**  
  Menambahkan customer baru  
  ![POST Customer](images/post-customer.png)

- **`POST /customers/{id}/bookings`**  
  Customer melakukan pemesanan  
  ![POST Booking](images/post-booking.png)

- **`POST /customers/{id}/bookings/{id}/reviews`**  
  Customer memberikan ulasan  
  ![POST Review](images/post-review.png)

### PUT
- **`PUT /customers/{id}`**  
  Mengubah data customer  
  ![PUT Customer](images/put-customer.png)

---

## Endpoints: Vouchers

### GET
- **`GET /vouchers`**  
  Daftar semua voucher  
  ![GET Vouchers](images/get-vouchers.png)

- **`GET /vouchers/{id}`**  
  Detail informasi voucher  
  ![GET Voucher ID](images/get-voucher-id.png)

### POST
- **`POST /vouchers`**  
  Menambahkan voucher baru  
  ![POST Voucher](images/post-voucher.png)

### PUT
- **`PUT /vouchers/{id}`**  
  Mengubah data voucher  
  ![PUT Voucher](images/put-voucher.png)

### DELETE
- **`DELETE /vouchers/{id}`**  
  Menghapus voucher  
  ![DELETE Voucher](images/delete-voucher.png)

**ERROR 400**

**ERROR 401**

**ERROR 404**
