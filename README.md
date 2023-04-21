# Óriások Völgye: A Mágiaszív hősei
Készítette: Kozma Kristóf

## A játék futtatása IDEA fejlesztői környezetben:
 1. Nyissuk meg a projektet a File/Open menüponton, majd a mappa tallózásán keresztül.
 2. Nyissuk meg bal oldalt a Project fület, OriasokVolgyeKotprog/src/Jatek.java fájlra jobb klikk, és Run 'Jatek.main()'.
 3. Kezdődhet a játék!

## A játék futtatása Linuxon:
 1. Nyissuk meg a terminált a forráskódok mappájában (vagy a cd paranccsal menjünk oda)! (OriasokVolgyeKotprog/src)
 2. Fordítsuk le a programot az eggyel kijjebbi mappába: 
	javac Jatek.java -d ..
 3. Lépjünk vissza egy mappát:
	cd ..
 4. Futtassuk a játékot:
	java Jatek
 5. Amennyiben ékezet nélkül szeretnénk a szövegeket kiírni, tegyük hozzá futtatáskor a kapcsolót:
	java Jatek -nincsEkezet
 6. Kezdődhet a játék! (Érdemes a terminált az alapértelmezettnél nagyobbra állítani, a játék sok szöveggel operál.)

A Linux esetén használatos parancsok Windowson is működnek, habár ott a színezett szövegkiírás nem működik, helyette ANSI kódokat hagy a szövegben. A program futtatása Windows parancssorban nem ajánlott!

## Egyéb tudnivalók:
 - a játék konzolos
 - a játékban 4 faj (frakció) közül lehet választani, minden fajnak 4-4 egysége és 3-3 varázslata van, azaz összesen 16 különböző egység és 12 különböző varázslat érhető el a játékban (mindnek a játékban, vásárlás közben érhető el részletes leírása)
 - a játékban található leírások szövegében a "támadás" csak a támadásra vonatkozik, a visszatámadásra nem
 - a játékban konzolos parancsokon keresztül lehet cselekedni. Az összetettebb parancsokat igénylő helyeken mindig elérhető a "p" parancs, ennek beírásával a játék kilistázza az összes használható parancsot egy rövid leírással
 - a csatára való felkészülés egy hosszú folyamat, ezért a presets nevű mappába előre összeállított seregek megvásárlását és elhelyezését segítő szöveges fájlokat raktam. A _readme.txt nevű fájl rövid leírást tartalmaz a használatukról, magukról az előre összeállított seregekről és velük kapcsolatban néhány stratégiáról
