# **API JAVA OOP | SISTEM PEMESANAN VILA**  

**Mata Kuliah:** Pemrograman Berorientasi Objek B  
**Dosen Pengampu:** Wayan Oger Vihikan, S.T.I., M.I.T.  
**Semester:** 2/Genap


## **🌐 INTRODUCING** 
Tugas ini merupakan proyek pembuatan backend API sederhana sebagai pemenuhan tugas akhir mata kuliah Pemrograman Berorientasi Objek, menggunakan bahasa Java dengan pendekatan OOP. API dirancang untuk sistem pemesanan vila yang mencakup manajemen data vila, tipe kamar, pemesanan, ulasan pelanggan, dan voucher diskon. Komunikasi dilakukan melalui metode HTTP (GET, POST, PUT, DELETE) dengan format data JSON dan basis data SQLite. Pengujian dilakukan menggunakan Postman, serta akses dibatasi dengan API key yang di-hardcode.


## **🧑‍💻 MEMBER**
* **Anggota 1:** NI MADE ADELIA WIRASANTI - [2405551010] 
* **Anggota 2:** NI PUTU CANDRADEVI DAVANTARI - [2405551035]
* **Anggota 3:** KHARISMA ROSARY FITRI SANDA - [2405551072]
* **Anggota 4:** NI LUH PUTU INDAH SUARI - [2405551103]


## **🛠️ STRUCTURE**  
Struktur program proyek ini dibuat secara modular dengan prinsip OOP agar kode lebih rapi, mudah dipelihara, dan dikembangkan. Seluruh kode berada dalam folder src dan dibagi ke beberapa package sesuai fungsinya:
* **Package models →** Berisi class entitas utama seperti Booking, Customer, Review, Room, Villa, dan Voucher yang mencerminkan tabel pada database SQLite.
* **Package controllers →** Mengatur logika bisnis seperti validasi input, pemrosesan data, dan interaksi dengan database.
* **Package routes →** Menyediakan endpoint HTTP (GET, POST, PUT, DELETE) dan mengarahkan request ke controller.
* **Package queries →** Menyimpan query SQL untuk akses data, dipisahkan dari logika bisnis.
* **Package database →** Mengelola koneksi dan inisialisasi database SQLite.
* **Package exceptions →** Menangani error seperti data tidak ditemukan atau input tidak valid.
* **Package utils →** Menyediakan fungsi bantu seperti validasi email, parsing JSON, dan pengecekan tanggal.
* **Package core →** Menjalankan server HTTP, menyetel API key, dan memproses permintaan dari pengguna.


## **🔐 AUTENTIKASI**
Akses ke endpoint API dilakukan menggunakan autentikasi Bearer Token, yang berfungsi sebagai kunci akses untuk memastikan hanya pihak yang memiliki izin yang dapat mengakses dan memanipulasi data.

Gunakan token berikut pada setiap request:

<pre> Authorization: Bearer Token API1234 </pre> 


## **⚠️ ERROR HANDLING**
Program aplikasi ini dilengkapi sistem penanganan error menggunakan konsep Exception di Java. Jika terjadi kesalahan saat memproses request, server akan merespons dengan status HTTP dan pesan error dalam format JSON. Contohnya:
* 400 Bad Request jika data tidak lengkap atau format tidak valid (seperti email salah atau kapasitas bukan angka).
* 401 Unauthorized jika API key tidak disertakan atau tidak sesuai.
* 404 Not Found jika ID data tidak ditemukan.


## **⚙️ TEST IN POSTMAN**
Pengujian program dilakukan menggunakan Postman, dengan server API yang berjalan di localhost:8080. Meskipun dapat diakses melalui browser, Postman digunakan agar seluruh metode request seperti GET, POST, PUT, dan DELETE dapat diuji secara menyeluruh dan efisien.
## Villa Endpoints

### GET
- **`GET /villas`**
  Mengambil data semua villa yang tersedia
  ![GET /villas](https://github.com/adeliawirasanti/Pembuatan-API-JAVA-OOP-Pemesanan-Vila/blob/main/images/%5B1%5DGET-VILLA.png)

- **`GET /villas/{id}`**  
  Informasi detail suatu vila  
 ![GET /villas/id](https://github.com/adeliawirasanti/Pembuatan-API-JAVA-OOP-Pemesanan-Vila/blob/main/images/%5B2%5DGET-VILLA-ID.png)

- **`GET /villas/{id}/rooms`**  
  Informasi kamar suatu vila, lengkap dengan fasilitas dan harga  
  ![GET /villas/id/rooms](https://github.com/adeliawirasanti/Pembuatan-API-JAVA-OOP-Pemesanan-Vila/blob/main/images/%5B3%5DGET-VILLA-ID-ROOM.png)

- **`GET /villas/{id}/bookings`**  
  Daftar semua booking pada suatu vila  
  ![GET /villas/id/bookings](https://github.com/adeliawirasanti/Pembuatan-API-JAVA-OOP-Pemesanan-Vila/blob/main/images/%5B4%5DGET-VILLA-ID-BOOKING.png)

- **`GET /villas/{id}/reviews`**  
  Daftar semua review pada suatu vila  
![GET /villas/id/reviews](https://github.com/adeliawirasanti/Pembuatan-API-JAVA-OOP-Pemesanan-Vila/blob/main/images/%5B5%5DGET-VILLA-ID-REVIEW.png)

- **`GET /villas?ci_date={checkin_date}&co_date={checkout_date}`**  
  Pencarian ketersediaan vila berdasarkan tanggal check-in dan checkout  
  ![GET Availability](https://github.com/adeliawirasanti/Pembuatan-API-JAVA-OOP-Pemesanan-Vila/blob/main/images/GET-VILLA-CI-CO-DATE.png)

### POST
- **`POST /villas`**  
  Menambahkan data vila  
 ![POST /villas](https://github.com/adeliawirasanti/Pembuatan-API-JAVA-OOP-Pemesanan-Vila/blob/main/images/POST-VILLA.png)

- **`GET /villas, setelah POST villas`** 
Mengambil data semua villa yang tersedia setelah melakukan post.
![GET /villas](https://github.com/adeliawirasanti/Pembuatan-API-JAVA-OOP-Pemesanan-Vila/blob/main/images/%5B8%5DGET-VILLA-SETELAH-POST.png)

- **`POST /villas/{id}/rooms`**  
  Menambahkan tipe kamar pada vila  
 ![POST /villaS/id/rooms](https://github.com/adeliawirasanti/Pembuatan-API-JAVA-OOP-Pemesanan-Vila/blob/main/images/%5B9%5DPOST-VILLA-ID-ROOM.png)

- **`GET /villas, setelah POST villas/{id}/rooms`**
  Melihat data terbaru dari villa tersebut, termasuk kamar yang baru saja ditambahkan
  ![GET /villas/id/rooms](https://github.com/adeliawirasanti/Pembuatan-API-JAVA-OOP-Pemesanan-Vila/blob/main/images/%5B10%5DGET-VILLA-SETELAH-POST-VILLA-ID-ROOM.png)

### PUT
- **`PUT /villas/{id}`**  
  Mengubah data suatu vila  
  ![PUT /villas/id](https://github.com/adeliawirasanti/Pembuatan-API-JAVA-OOP-Pemesanan-Vila/blob/main/images/%5B11%5DPUT-VILLA%20ID%20.png)

- **`GET /villas, setelah put`**
    mengambil data terbaru dari semua villa, termasuk villa yang barusan di-update
  ![GET /villas](https://github.com/adeliawirasanti/Pembuatan-API-JAVA-OOP-Pemesanan-Vila/blob/main/images/%5B12%5DGET-VILLA-SETELAH-PUT.png)

- **`PUT /villas/{id}/rooms/{id}`**  
  Mengubah informasi kamar suatu vila  
  ![PUT Room](https://github.com/adeliawirasanti/Pembuatan-API-JAVA-OOP-Pemesanan-Vila/blob/main/images/%5B13%5DPUT-VILLA-ID-ROOM-ID.png)

### DELETE
- **`DELETE /villas/{id}/rooms/{id}`**  
  Menghapus kamar suatu vila  
  ![DELETE /villas/id/rooms/id](https://github.com/adeliawirasanti/Pembuatan-API-JAVA-OOP-Pemesanan-Vila/blob/main/images/%5B16%5DDELETE-VILLA-ID-ROOM-ID.png)

- **`GET /villas, setelah delete villas/{id}/rooms/{id}`**
  Memastikan perubahan berhasil dan kamar tersebut benar-benar tidak ada lagi dalam data villa.
  ![GET /villas/id/rooms/id](https://github.com/adeliawirasanti/Pembuatan-API-JAVA-OOP-Pemesanan-Vila/blob/main/images/%5B17%5DGET-VILLA-ID-ROOM-SETELAH-DELETE-VILLA-ID-ROOM-ID.png)

- **`DELETE /villas/{id}`**  
  Menghapus data suatu vila  
  ![DELETE /villas/id](https://github.com/adeliawirasanti/Pembuatan-API-JAVA-OOP-Pemesanan-Vila/blob/main/images/%5B14%5DDELETE-VILLA-ID.png)

- **`GET /villas, setelah delete villas/{id}`**
  Menampilkan daftar villa terbaru setelah satu villa dihapus
  ![GET /villas/id](https://github.com/adeliawirasanti/Pembuatan-API-JAVA-OOP-Pemesanan-Vila/blob/main/images/%5B15%5DGET-VILLA-ID-SETELAH-DELETE.png)

---

## Customers Endpoints

### GET
- **`GET /customers`**  
  Daftar semua customer  
  ![Get /customers](https://github.com/adeliawirasanti/Pembuatan-API-JAVA-OOP-Pemesanan-Vila/blob/main/images/%5B1%5DGET-CUSTOMER.png)

- **`GET /customers/{id}`**  
  Informasi detail seorang customer  
  ![Get /customers/id](https://github.com/adeliawirasanti/Pembuatan-API-JAVA-OOP-Pemesanan-Vila/blob/main/images/%5B2%5DGET-CUSTOMER-ID.png)

- **`GET /customers/{id}/bookings`**  
  Daftar booking yang telah dilakukan  
  ![Get /customers/id/bookings](https://github.com/adeliawirasanti/Pembuatan-API-JAVA-OOP-Pemesanan-Vila/blob/main/images/%5B3%5DGET-CUSTOMER-ID-BOOKING.png)

- **`GET /customers/{id}/reviews`**  
  Daftar ulasan dari customer  
  ![Get /customers/id/reviews](https://github.com/adeliawirasanti/Pembuatan-API-JAVA-OOP-Pemesanan-Vila/blob/main/images/%5B4%5DGET-CUSTOMER-ID-REVIEW.png)

### POST
- **`POST /customers`**  
  Menambahkan customer baru  
  ![Post /customers](https://github.com/adeliawirasanti/Pembuatan-API-JAVA-OOP-Pemesanan-Vila/blob/main/images/%5B5%5DPOST-CUSTOMER.png)

- **`GET /customers/{id}`**
  Mengambil detail data satu customer berdasarkan id
![GET /customers/id](https://github.com/adeliawirasanti/Pembuatan-API-JAVA-OOP-Pemesanan-Vila/blob/main/images/%5B6%5DGET-CUSTOMER-ID-SETELAH-POST.png)

- **`POST /customers/{id}/bookings`**  
  Customer melakukan pemesanan  
  ![Post /customers/id/bookings](https://github.com/adeliawirasanti/Pembuatan-API-JAVA-OOP-Pemesanan-Vila/blob/main/images/%5B7%5DPOST-CUSTOMER-ID-BOOKING.png)

- **`GET /customers/{id}/bookings, setelah POST`**
  Mengambil semua data booking milik customer berdasarkan id, termasuk booking terbaru yang baru saja dibuat.
  ![GET /customers/id/bookings](https://github.com/adeliawirasanti/Pembuatan-API-JAVA-OOP-Pemesanan-Vila/blob/main/images/%5B8%5DGET-VILLA-SETELAH-POST.png)

- **`POST /customers/{id}/bookings/{id}/reviews`**  
  Customer memberikan ulasan  
  ![Post /customers/id/bookings/id/reviews](https://github.com/adeliawirasanti/Pembuatan-API-JAVA-OOP-Pemesanan-Vila/blob/main/images/%5B9%5DPOST-CUSTOMER-ID-BOOKING-ID-REVIEW.png)

### PUT
- **`PUT /customers`**
Mengedit/update data customer yang sudah ada.
![PUT /customers](https://github.com/adeliawirasanti/Pembuatan-API-JAVA-OOP-Pemesanan-Vila/blob/main/images/%5B10%5DPUT-CUSTOMER.png)

- **`GET /customers/{id}, setelah PUT customers`**
  Mengambil data terbaru dari customer yang barusan diubah
  ![GET customers/id](https://github.com/adeliawirasanti/Pembuatan-API-JAVA-OOP-Pemesanan-Vila/blob/main/images/%5B11%5DGET-CUSTOMER-ID-SETELAH-PUT.png)

---

## Vouchers Endpoints

### GET
- **`GET /vouchers`**  
  Daftar semua voucher  
  ![Get /vouchers](https://github.com/adeliawirasanti/Pembuatan-API-JAVA-OOP-Pemesanan-Vila/blob/main/images/%5B1%5DGET-VOUCHER.png)

- **`GET /vouchers/{id}`**  
  Detail informasi voucher  
  ![Get /vouchers/id](https://github.com/adeliawirasanti/Pembuatan-API-JAVA-OOP-Pemesanan-Vila/blob/main/images/%5B2%5DGET-VOUCHER-ID.png)

### POST
- **`POST /vouchers`**  
  Menambahkan voucher baru  
  ![Post /vouchers](https://github.com/adeliawirasanti/Pembuatan-API-JAVA-OOP-Pemesanan-Vila/blob/main/images/%5B3%5DPOST-VOUCHER.png)

- **`GET /vouchers, setelah POST`**  
Mengambil daftar voucher terbaru, termasuk voucher yang baru saja ditambahkan.
![GET /vouchers](https://github.com/adeliawirasanti/Pembuatan-API-JAVA-OOP-Pemesanan-Vila/blob/main/images/%5B4%5DGET-VOUCHER-SETELAH-POST.png)

### PUT
- **`PUT /vouchers/{id}`**  
  Mengubah data voucher  
  ![Put /vouchers/id](https://github.com/adeliawirasanti/Pembuatan-API-JAVA-OOP-Pemesanan-Vila/blob/main/images/%5B5%5DPUT-VOUCHER.png)

- **`GET /vouchers/{id}, setelah PUT`**
  Mengambil data voucher yang sudah diperbarui dari server.
  ![GET /vouchers/id](https://raw.githubusercontent.com/adeliawirasanti/Pembuatan-API-JAVA-OOP-Pemesanan-Vila/main/images/%5B6%5DGET-VOUCHER-ID-SETELAH-PUT.png)

### DELETE
- **`DELETE /vouchers/{id}`**  
  Menghapus voucher  
 ![Delete /vouchers/id](https://github.com/adeliawirasanti/Pembuatan-API-JAVA-OOP-Pemesanan-Vila/blob/main/images/%5B7%5DDELETE-VOUCHER.png)

 - **`GET /vouchers/{id}, setelah DELETE`**
   Memastikan bahwa data benar-benar sudah terhapus dari database, bukan hanya hilang dari tampilan tapi masih tersimpan.
   ![GET /vouchers](https://github.com/adeliawirasanti/Pembuatan-API-JAVA-OOP-Pemesanan-Vila/blob/main/images/%5B8%5DGET-VOUCHER-ID-SETELAH-DELETE.png)

---

## ERROR 400
400 Bad Request
Artinya: Permintaan dari client salah atau tidak sesuai format.
![ERROR 404](https://github.com/adeliawirasanti/Pembuatan-API-JAVA-OOP-Pemesanan-Vila/blob/main/images/ERROR%20400.png)

## ERROR 401
401 Unauthorized
Artinya: Client belum melakukan otentikasi atau token/kredensial tidak sah.
![ERROR 401](https://github.com/adeliawirasanti/Pembuatan-API-JAVA-OOP-Pemesanan-Vila/blob/main/images/ERROR%20401.png)

## ERROR 404
404 Not Found
Artinya: Resource yang diminta tidak ditemukan di server.
![ERROR 404](https://github.com/adeliawirasanti/Pembuatan-API-JAVA-OOP-Pemesanan-Vila/blob/main/images/ERROR%20404.png)
