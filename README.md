# **✨ API Pemesanan Vila**  
<mark>Tugas 2 PBO- Sistem Manajemen Vila</mark> 

## **📌 Deskripsi Proyek** 
Program ini adalah sebuah API (Application Programming  Interface) yang dirancang untuk mengelola pemesanan vila. API ini menyediakan berbagai endpoint untuk melakukan operasi CRUD (Create, Read, Update, dan Delete) pada entitas-entitas seperti vila 🏠, pelanggan 👥, pemesanan 📅, dan lainnya. API dibangun dengan menggunakan bahasa pemrograman Java dan database SQLite untuk menyimpan data. Setiap request yang dikirim ke API harus disertai dengan API key yang valid untuk otentikasi, yang di hardcode dalam kelas Main sebagai lapisan keamanan dasar.

## **🔧 Fitur Utama**  
API ini mendukung beberapa jenis HTTP request method, yaitu :
* GET: Untuk mengambil daftar atau detail data entitas.
* POST: Untuk membuat entitas baru, dengan validasi kelengkapan dan format data (seperti email dan nomor telepon).
* PUT: Untuk memperbarui data entitas yang sudah ada.
* DELETE: Untuk menghapus data entitas.

## ⚠️ Error Handling

API ini menggunakan sistem error handling terstruktur yang mengembalikan respons JSON standar ketika terjadi kesalahan. Setiap error response akan menyertakan:  
- Kode status HTTP yang jelas  
- Pesan error deskriptif  
- Timestamp untuk keperluan debugging  

Berikut adalah daftar error yang umum terjadi:

| Kode Error | Penyebab                  | Solusi                     |
|------------|---------------------------|----------------------------|
| 400        | Data tidak valid          | Periksa kelengkapan dan format data yang dikirim |
| 401        | API Key tidak valid       | Pastikan header `X-API-KEY` berisi kunci yang benar |
| 404        | Data tidak ditemukan      | Verifikasi ID yang diminta tersedia di database |

## ✅ Pengujian dan Integrasi API

API dapat diuji menggunakan Postman dengan mengirim request ke endpoint yang tersedia. Database SQLite yang digunakan telah disiapkan dengan skema relasional lengkap, membuat API ini siap diintegrasikan dengan:
- Aplikasi website (React, Angular, dll)
- Aplikasi mobile (Android/iOS)
- Sistem POS lainnya
