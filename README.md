🗳️ E-Voting Portal — Secure Role-Based Online Voting System

A modern, secure, and interactive E-Voting application built in Java, designed to simplify and digitize the entire election process — from authentication to live tracking and result visualization.

🧭 Overview

The E-Voting Portal is a role-based online voting system that ensures secure, transparent, and time-bound elections.
It provides real-time monitoring, role-specific controls, and graphical result visualization, all powered by Java and SQL for backend integration.

⚙️ Key Features
🔐 Authentication & Role-Based Access

Super Admin – Declares results and terminates elections.

Admin – Starts, pauses, and resumes elections; monitors voter activity.

Voter – Authenticates securely and casts their vote within the allowed time.

🕒 Voting System

⏳ Timed Voting – Voting is available only during a set time window.

⛔ Hold & Resume Elections – Admins can pause and resume voting anytime.

📈 Live Vote Count – Real-time tracking of total votes cast.

🔒 Secure Login – Password-based authentication for all roles.

📊 Results & Visualization

🏁 Result Declaration – Super Admin officially announces final results.

📉 Graphical Results – Displays candidate performance via charts and graphs for easy understanding.

🗄️ Database Configuration
Configuration	Details
Database Used	SQL (MySQL / PostgreSQL etc.)
Connection Method	JDBC
Setup	Create required tables before running the app
Credentials	Update in config.properties file
🧩 Usage Guide

1️⃣ Login as Admin, Super Admin, or Voter.
2️⃣ Admin initiates the election and monitors live statistics.
3️⃣ Voters cast their votes securely within the active voting window.
4️⃣ Super Admin declares the final results and ends the election.
5️⃣ Results are shown in graphical format for better insights.

🧠 Technologies Used
Category	Tools / Libraries
Language	Java (JDK 17+)
GUI	Swing / JavaFX
Database	SQL via JDBC
Visualization	JFreeChart (optional)
Version Control	Git & GitHub
🎯 Highlights

✨ Role-based access ensures security & accountability
🕒 Timed voting makes elections structured & fair
📊 Real-time vote visualization enhances transparency
⚡ Built fully in Java with clean JDBC integration
