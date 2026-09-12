1. Szállítási díj

Tesztesetek
    Rendelési érték	Tömeg	Ország	Expressz	Elvárt eredmény 	Tényleges eredmény	Státusz
1	10 000	          3	    "HU"	false	            1 500	        1 500         	Pass
2	10 000	          5	    "HU"	false	            2 500	        5 000       	Fail
3	10 000	          10	"HU"	false	            2 500	        2 500       	Pass
4	10 000	          20	"HU"	false	            2 500		    5 000	        Fail
5	10 000	          25	"HU"	false	            5 000	      	5 000	        Pass
6	20 000	          10	"HU"	false	            2 500	       	0	            Fail
7	20 001	          10	"HU"	false	               0	        0	            Pass
8	10 000	           3	"hu"	false	            1 500	        3 000       	Fail
9	10 000	          10	"AT"	false	            5 000	        5 000       	Pass
10	10 000	          10	"AT"	true	            7 500	        5 050       	Fail

Megtalált hibák és reprodukálásuk
1. Hiba: Az expressz szállítás felára hibásan van kiszámolva
-A specifikáció szerint az alapértelmezett szállítási díj 50%-kal növekszik, a kódban viszont csak fix 50 Ft-ot adnak hozzá (shippingCost + 50 helyett shippingCost * 1.5 kellene).
-Reprodukáló teszteset: TC10 (Külföldi, 10 kg, expressz). Elvárt: 7 500 Ft. Kód eredménye: 5 050 Ft.

2. Hiba: Hibás tömeghatár-kezelés (Boundary Limits)
-A "5 kg-tól 20 kg-ig 2500 Ft" kategóriát a kód weight > 5 && weight < 20 feltétellel vizsgálja. Emiatt a kerek 5 kg-os és 20 kg-os csomagok átcsúsznak az else ágba, és irreálisan magas, 5000 Ft-os díjat kapnak.
-Reprodukáló teszteset: TC02 (Pont 5 kg), TC04 (Pont 20 kg). Elvárt: 2 500 Ft. Kód eredménye: 5 000 Ft.

3. Hiba: Kis- és nagybetűk érzékenysége az országkódnál
-A specifikáció kimondja, hogy az országkód kis- és nagybetűvel is megadható. A kód viszont szigorúan egyezést (=== "HU") vizsgál, így a kisbetűs "hu" érték esetén tévesen külföldi, dupla szállítási díjat számol. Ráadásul az ingyenes szállítás ellenőrzésénél is elbukik.
-Reprodukáló teszteset: TC08 (Bemenet: "hu"). Elvárt: 1 500 Ft. Kód eredménye: 3 000 Ft.

4. Hiba: Az ingyenes szállítás értékhatára pontatlan
-A specifikáció "20 000 Ft feletti" értéket mond, ami szigorúan nagyobb (> 20000) értéket jelent. A kód >= 20000 operátort használ, így a pontosan 20 000 Ft-os rendelésnél is már ingyenes a szállítás.
-Reprodukáló teszteset: TC06 (Rendelési érték: 20 000 Ft). Elvárt: 2 500 Ft. Kód eredménye: 0 Ft.

5. Hiba: Hiányzó bemeneti validáció a negatív értékekre
-A specifikáció rögzíti, hogy "A rendelési érték és a tömeg nem lehet negatív." A kód nem ellenőrzi ezt, negatív súly esetén weight < 5 alapján 1500 Ft-ot, negatív kosárérték esetén normál árat ad vissza hibajelzés vagy kivételdobás (Exception) helyett.
-Reprodukáló teszteset: TC11 (Érték: -5 000 Ft) és TC12 (Tömeg: -2 kg). Elvárt: Programhiba/Kivétel. Kód eredménye: Lefut és díjat ad vissza.

Plaintext
test: Szállítási díj kalkulátor teszteseteinek és hibalistájának hozzáadása

- 12 db teszteset létrehozása és dokumentálása a követelmények alapján
- Manuális tesztfuttatás elvégzése
- 5 kritikus hiba azonosítása (expressz felár, határértékek, case-sensitivity, értékhatár és bemeneti validáció hiánya)
- Hibalista és reprodukáló lépések csatolása a review-hoz

2.DiscountCalculator
Tesztesetek
Rendelési érték VIP Elvárt Tényleges Státusz
19 000    false        9    0009 000    Pass
210 000     false10 00010 000           Pass
320 000     false19 00019 999.95        Fail
425 000     false22 50024 999.90        Fail
550 000     false42 50049 999.90        Fail
6100 000    false85 00099 999.85        Fail
720 000     true18 00019 999.80         Fail
8100 000    true80 00099 999.80          Fail
9-5 000 false Hiba / Kivétel-5 000      Fail
100trueHiba / Kivétel-0.2               Fail

Megtalált hibák és reprodukálásuk
1. A kedvezmény összegének hibás kiszámításaA függvény a rendelési értékből (orderValue) közvetlenül a százalékos szorzót (discount, pl. 0.05) vonja ki, nem pedig a kiszámolt kedvezmény összegét (pl. orderValue * discount).
-Reprodukáló teszteset: 3 (20 000 Ft). Elvárt: 19 000 Ft. Kód eredménye: 19 999.95.
2. Hibás operátorok a határértékeknél (10 000, 25 000, 50 000)10 000 Ft-nál a kód > 10000-et vizsgál, így ha pont 10 000 a rendelés, nincs kedvezmény, pedig a specifikáció szerint "10 000 Ft-tól" jár. 
Ugyanez a hiba fennáll az 50 000 Ft-os határnál is (<= 50000 miatt a 10%-os sávba esik a 15% helyett).
-Reprodukáló teszteset: 5 (Pont 50 000 Ft). Elvárt: 42 500 Ft (15% kedvezmény). Kód eredménye: 49 999.90 (10%-os sáv szerint számol és a kivonás is rossz).
4. VIP kedvezmény hibás hozzáadásaA kód a tizedestörtként kezelt kedvezményhez (0.05) egész számként 5-öt ad hozzá, ami így 5.05 (505%) lesz. 
Emiatt a VIP ügyfelek mindig beleütköznek a 20%-os limitbe (0.20). Helyesen discount += 0.05 lenne.
-Reprodukáló teszteset: 7 (20 000 Ft, VIP). Elvárt: 18 000 Ft. Kód eredménye: 19 999.80 (20%-os limit miatt 0.20-at von ki).
5. Hiányzó bemeneti validáció negatív értékekreA specifikáció szerint negatív érték nem megengedett.
A kód nem dob hibát, hanem tovább fut és negatív értéket ad vissza.
-Reprodukáló teszteset: 9 (-5 000 Ft). Elvárt: Hiba / Kivétel. Kód eredménye: -5 000.

Plaintexttest: 
DiscountCalculator tesztesetek és hibalista hozzáadása10 db teszteset létrehozása és dokumentálása a követelmények alapjánManuális tesztfuttatás elvégzése4 kritikus hiba azonosítása (összegszámítás logikai hibája, rossz VIP matek, határérték problémák, hiányzó validáció)
Hibalista és reprodukáló lépések csatolása a review-hoz

ParkingFeeCalculator
Tesztesetek
ID  Percek  Hétvége VIP Elvárt Tényleges Státusz
110falsefalse00Pass
215falsefalse00Pass
316falsefalse6000Fail
460falsefalse600600Pass
561falsefalse1 200600Fail
6120truefalse600600Pass
7120falsetrue9601 180Fail
8120truetrue480580Fail
9600falsefalse5 0005 000Pass
10-10falsefalseHiba / Kivétel0Fail

Megtalált hibák és reprodukálásukHiba: 
1.Hibás "megkezdett óra" számításA kód a Math.floor() (lefelé kerekítés) függvényt használja a Math.ceil() (felfelé kerekítés) helyett. 
Emiatt 16 és 59 perc között 0 órát számol (díj = 0 Ft), és 61 percre is csak 1 órát számol.
-Reprodukáló teszteset: 3 (16 perc). Elvárt: 600 Ft. Kód eredménye: 0 Ft.
2.A VIP kedvezmény hibás levonásaA specifikáció 20% kedvezményt ír elő a VIP ügyfeleknek. 
A kód azonban fee = fee - 20 formában 20 Forintot von le az összegből százalékos csökkentés (fee * 0.8) helyett.
-Reprodukáló teszteset: 7 (120 perc, VIP). Elvárt: 960 Ft. Kód eredménye: 1 180 Ft.
3.Negatív parkolási idő kezeléseA rendszernek hibát kellene jeleznie negatív bemenet esetén, de az első if (minutes < 15) feltétel miatt egyszerűen 0 Ft-ot ad vissza.
-Reprodukáló teszteset: 10 (-10 perc). Elvárt: 
4. Kivétel. Kód eredménye: 0 Ft.

Plaintexttest: 
ParkingFeeCalculator tesztesetek és hibalista hozzáadása10 db teszteset létrehozása és dokumentálása a követelmények alapjánManuális tesztfuttatás elvégzése3 kritikus hiba azonosítása (Math.floor használata Math.ceil helyett,
hibás VIP kedvezmény levonás, 
negatív idő validációjának hiánya)
Hibalista és reprodukáló lépések csatolása a review-hoz

3.CinemaTicketCalculator
Tesztesetek
ID  Életkor Diák    3D  Elvárt Tényleges Státusz
15falsefalse00Pass
26falsefalse2 1000Fail
317falsefalse2 1002 100Pass
425falsefalse3 0003 000Pass
565falsefalse1 8003 000Fail
616truefalse2 1001 680Fail
720truetrue3 2004 320Fail
820falsetrue3 8005 400Fail
9-5falsefalseHiba / Kivétel0Fail
10125falsefalseHiba / Kivétel1 800Fail

Megtalált hibák és reprodukálásuk
1. A kedvezmények halmozódnak a maximális kiválasztása helyettA specifikáció kimondja, hogy egyszerre csak a legnagyobb kedvezmény alkalmazható. 
A kód az életkor alapú módosítások után egy független if-ben rászorozza a diák kedvezményt (0.8), így egy 16 éves diák 30% + 20% halmozott kedvezményt kap.
-Reprodukáló teszteset: 6 (16 év, diák). Elvárt: 2 100 Ft. Kód eredménye: 1 680 Ft.
2. A 3D felár százalékos szorzóként van megadvaA 3D film esetén 800 Ft fix felár fizetendő az ár után (kedvezmény nem vonatkozik rá). 
A kód ezzel szemben az addigi árat szorozza be 1.8-cal (price * 1.8).
-Reprodukáló teszteset: 8 (20 év, 3D). Elvárt: 3 800 Ft. Kód eredménye: 5 400 Ft.
3. Hibás korhatár vizsgálatok (6 és 65 év)"6 éves kor alatt" < 6-ot jelent, a kód <= 6-ot használ, így a 6 évesek ingyen mennek be. 
A "65 éves kortól" >= 65-öt jelent, a kód > 65-öt vizsgál, így a 65 évesek nem kapnak nyugdíjas kedvezményt.
4.Reprodukáló teszteset: 2 (6 év). Elvárt: 2 100 Ft. Kód eredménye: 0 Ft.
5. Érvénytelen életkor validációjának hiánya
Negatív vagy 120 év feletti kor esetén a specifikáció szerint hibát kell jelezni.
A kód egyszerűen tovább számol velük.
-Reprodukáló teszteset: 10 (125 év). Elvárt:
6. / Kivétel. Kód eredménye: 1 800 Ft.

Plaintexttest: CinemaTicketCalculator tesztesetek és hibadokumentáció10 db teszteset létrehozása és dokumentálása a követelmények alapján
Manuális tesztfuttatás elvégzése
4 kritikus hiba azonosítása (halmozódó kedvezmények, hibás % alapú 3D felár, pontatlan korhatárok, hiányzó kor-validáció)
Hibalista és reprodukáló lépések csatolása a review-hoz

ExamGradeCalculator
Tesztesetek
IDElméletGyakorlatElvárt eredményTényleges eredményStátusz
1302022Pass
2203012Fail
3301011Pass
4303032Fail
5353543Fail
6454054Fail
7604055Pass
8-520Hiba / Kivétel1Fail
97040Hiba / Kivétel5Fail
103050Hiba / Kivétel4Fail

Megtalált hibák és reprodukálásuk
1. Hiba logikai operátor a bukás (minimum pont) feltételénélA theory < 30 && practice < 20 azt jelenti, hogy a kód csak akkor buktat, ha a hallgató mindkét részből megbukott. 
Helyesen || (VAGY) operátor kellene, mert bármelyik minimum el nem érése bukást von maga után.
-Reprodukáló teszteset: 2 (Elmélet: 20, Gyakorlat: 30). Elvárt: 1. Kód eredménye: 2.
2. Elcsúszott ponthatárok az értékelésnélA specifikáció szerint 60-69 pont a 3-as, de a kód total <= 60 feltétellel 2-est ad 60 pontra. Ugyanígy 70 pontra 3-ast, és 85 pontra 4-est ad az elvárt jegy helyett.
-Reprodukáló teszteset: 4 (30 elmélet, 30 gyakorlat = 60 pont). Elvárt: 3. Kód eredménye: 2.
3. Érvénytelen pontszámok validációjának hiányaA rendszer nem vizsgálja, hogy az elmélet 0-60, a gyakorlat 0-40 között van-e. 
Egy érvénytelen túlpontozott tesztet elfogad és kiszámolja rá az eredményt.
-Reprodukáló teszteset: 9 (70 elmélet, 40 gyak). Elvárt: Hiba / Kivétel. Kód eredménye: 

5.Plaintexttest: 
ExamGradeCalculator tesztesetek és hibalista hozzáadása10 db teszteset létrehozása és dokumentálása a követelmények alapján
Manuális tesztfuttatás elvégzése3 kritikus hiba azonosítása (ÉS operátor használata VAGY helyett, elcsúszott ponthatárok az egyenlőségjelek miatt, bemeneti validáció hiánya)
Hibalista és reprodukáló lépések csatolása a review-hoz

PackageClassifier
Tesztesetek
IDTömegHosszSzélMag.Elvárt eredményTényleges eredményStátusz
11202020"SMALL""SMALL"Pass22303030"SMALL""MEDIUM"Fail
310606060"MEDIUM""MEDIUM"Pass45801010"LARGE""MEDIUM"Fail
530120120120"LARGE""LARGE"Pass6151305050"OVERSIZE""OVERSIZE"Pass
735505050"OVERSIZE""OVERSIZE"Pass80101010"INVALID""SMALL"Fail
95-51010"INVALID""MEDIUM"Fail10101001010"OVERSIZE""MEDIUM"Fail

Megtalált hibák és reprodukálásuk
1. A SMALL kategória relációs jelei hibásakA "legfeljebb 2 kg" és "legfeljebb 30 cm" <= feltételt követel.
A kód < 2 és < 30-at vizsgál, így a kerek határértékek kicsúsznak a kategóriából.
-Reprodukáló teszteset: 2 (2 kg, 30x30x30). Elvárt: "SMALL". Kód eredménye: "MEDIUM".
2. A MEDIUM kategória "VAGY" operátort használ az "ÉS" helyettA specifikáció szerint "egyik mérete sem nagyobb 60 cm-nél" (tehát minden oldala <= 60). A kód length <= 60 || width <= 60 || height <= 60 feltételt használ, ami alapján ha akár csak egy oldala 60 alatti, már MEDIUM lesz a csomag, függetlenül attól, hogy egy másik oldala pl. 100 cm.
-Reprodukáló teszteset: 10 (10 kg, 100x10x10). Elvárt: "OVERSIZE" (vagy LARGE). Kód eredménye: "MEDIUM".
3.Hiba: Az INVALID állapot (0 vagy negatív érték) nincs kezelveHa a tömeg vagy bármelyik méret 0 vagy negatív, a kódnak "INVALID" karakterláncot kellene visszaadnia. 
Ezt a kód sehol sem vizsgálja.
-Reprodukáló teszteset: 8 (Tömeg: 0). Elvárt: "INVALID". Kód eredménye: "SMALL" (mivel 0 < 2).

Plaintexttest: 
PackageClassifier tesztesetek és hibajelentés hozzáadása10 db teszteset létrehozása és dokumentálása a követelmények alapján
Manuális tesztfuttatás elvégzése3 kritikus hiba azonosítása (hibás SMALL határérték, hibás OR logika a MEDIUM ágon, hiányzó INVALID ág és validáció)
Hibalista és reprodukáló lépések csatolása a review-hoz