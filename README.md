Overview-

The E-Voting Portal is a secure, role-based online voting system built using Java. It enables a streamlined voting process with authentication, timed voting, live vote tracking, and detailed result visualization.

Features-

Authentication & Role-Based Access

Super Admin: Declares results and terminates the election.

Admin: Manages the election process, including starting, pausing, and resuming voting.

Voter: Casts their vote securely within the voting timeframe.

Voting System-

Timed Voting: Voting is open only during a predefined period.

Hold & Resume Elections: Admin can pause and resume the election as needed.

Live Vote Count: Admin can monitor how many voters have cast their votes in real-time.

Secure Authentication: Voters, admins, and super admins log in using a username and password.

Results & Visualization - 

Results Declaration: Super Admin declares the results.

Graphical Representation: The results are displayed using charts and graphs for better visualization.


Database Configuration - 

Database Used: SQL (MySQL/PostgreSQL, etc.)

Connection Method: JDBC

Ensure the database is set up with required tables before running the application.

Update database credentials in the config.properties file.

Usage-

Login as either Admin, Super Admin, or Voter.

Admin starts the election and monitors live voting stats.

Voters cast their votes within the allowed time.

Super Admin declares the final results and terminates the election.

Results are displayed with visual charts.

Technologies Used - 

Java (JDK 17+)

Swing/JavaFX (for GUI, if applicable)

SQL & JDBC (for database management)

JFreeChart (for result visualization, if used)

Git & GitHub (for version control)
