**Comic Book Reader**
Overview
Comic Book Reader is a Java application designed to allow users to view, import, and manage digital comic books. Users can load .cbr, .cbz, and .nhlcomic files, view pages in sequence, continue reading from the last saved position, and invert colors for better readability.

**Features Comic Book Import**: Supports .cbr, .cbz, and .nhlcomic formats.
Progress Tracking: Allows users to resume comics from the last saved page or start from the beginning.
Image Inversion: Option to invert comic page colors.


**Installation Prerequisites**: Ensure Java 8 or above is installed.
Dependencies: Add required libraries, such as Jackson for JSON handling.

**Clone Repository:**
bash
Code kopiëren
git clone https://github.com/username/comic-book-reader.git
cd comic-book-reader

**Compile & Run:**
bash
Code kopiëren
javac -d bin src/com/comicbookreader/*.java
java -cp bin com.comicbookreader.Mainmenu

**Usage**
Launch the Mainmenu to view and import comics.
Select a comic to view from the list.
Use the Continue Comic button to resume from the last page.
Use the Start From Beginning button to restart the comic if previously read.

**Project**
Mainmenu: The main interface with comic list, buttons, and progression tracking.
Comicbook: Represents a comic, including pages and progression.
Page: Manages individual comic pages and images.
Parsers (CBRParser, CBZParser): Extracts comic pages from respective formats.

JSON Storage
All comic progressions are saved to appdata/data.json. Each comic’s current page position is stored by name, enabling users to resume from the last saved point on startup.
