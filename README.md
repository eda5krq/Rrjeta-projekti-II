# Rrjeta-projekti-II
Projekti i dytë – Rrjetat Kompjuterike
**Tema nr. 5 – TCP**  
**Gjuha programuese: Java**  
**Mjedisi i testimit: Të paktën 4 pajisje të ndryshme në një rrjet real**

# Përshkrimi i projektit

Ky projekt implementon një sistem komunikimi **klient-server** duke përdorur **TCP sockets në Java**.  
Sistemi përbëhet nga:

- **Serveri**, i cili dëgjon klientët, menaxhon kërkesat, ruan mesazhet, kontrollon timeout-in, ofron qasje në file dhe ekspozon statistika përmes një HTTP serveri.
- **Klienti**, i cili lidhet me serverin, dërgon mesazhe dhe komanda, lexon përgjigjet dhe punon sipas privilegjeve të caktuara.

---

# Teknologjitë e përdorura

- Java
- TCP Sockets
- Threads / Runnable
- BufferedReader / PrintWriter
- File I/O
- HTTP Server i thjeshtë në Java

---

# Struktura e projektit

```text
Rrjeta-projekti-II/
├── src/
│   ├── Main.java
│   ├── TcpServer.java
│   ├── ClientHandler.java
│   ├── ServerState.java
│   ├── SimpleHttpServer.java
│   ├── FileManager.java
│   └── Client.java
├── shared/
│   └── (file-at e serverit)
├── README.md
└── .gitignore
