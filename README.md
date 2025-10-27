# TOTOLOTEK 
Zadanie zaliczeniowe z przedmiotu Programowanie Obiektowe realizowanego w semestrze letnim 2024/25. 

## Opis ogólny 
Celem zadania było stworzenie klas pozwalających na realizację systemu gry Totolotek. Totolotek sprzedaje zakłady pozwalające na uczestnictwo w grze i przeprowadza cyklicznie losowania 6 różnych liczb z 49. Gracze chcący wziąć udział w losowaniu typują swoje 6 liczb - jeden taki zakupiony typ nazywamy zakładem. Zakłady sprzedawane są jedynie poprzez sieć punktów zwanych kolekturami i jedynie w formie kuponów na okaziciela. Kupon może zawierać kilka zakładów i może obejmować więcej niż 1 losowanie. Pojedynczy zakład kosztuje 3 zł, z czego 0,60 zł to podatek, który Totolotek musi odprowadzić do budżetu państwa. Reszta zostaje do dyspozycji centrali Totolotka (na wygrane i zysk). Za każde trafienie trzech, czterech, pięciu lub sześciu liczb w pojedynczym zakładzie wypłacana jest nagroda, zwana odpowiednio nagrodą IV, III, II i I stopnia. Kolejne sekcje opisują dokładniej poszczególne elementy systemu.

### Centrala Totolotka
Centrala na swoją działalność dysponuje pewnymi środkami finansowymi. Może korzystać też z subwencji państwowych. Podstawową odpowiedzialnością centrali jest przeprowadzanie kolejnych losowań zwycięskich 6 liczb. Po każdym przeprowadzonym losowaniu centrala oblicza pulę nagród oraz wysokości wygranych każdego stopnia od I do IV. Są one obliczane w oparciu o łączną kwotę uzyskaną ze sprzedaży (minus podatek), przez wszystkie kolektury, wszystkich zakładów uczestniczących w danym losowaniu oraz w oparciu o liczby zakładów, które trafiły poszczególne wygrane I, II, III i IV stopnia. W obliczeniach tych uwzględniana jest także ewentualna kumulacja. Mechanizm kumulacji działa tak, że jeśli w danym losowaniu nikt nie trafi szóstki, to cała pula nagród I stopnia powiększa pulę I stopnia w kolejnym losowaniu, itd. Informacja o zwycięskiej szóstce, puli nagród I stopnia i kwotach wygranych każdego stopnia (tylko tych trafionych) muszą być dostępne publicznie. Centrala powinna ponadto potrafić wypisywać wyniki wszystkich przeprowadzonych losowań, wraz z podaniem:

- kwot wygranych każdego stopnia (tylko tych trafionych),
- liczb zwycięskich zakładów dla wygranych każdego stopnia,
- łącznej puli nagród każdego stopnia
  
oraz wypisywać stan swoich środków finansowych.

### Odbiór wygranych 
Gracz zgłasza się po odbiór wygranej (lub wygranych), okazując zwycięski kupon w kolekturze, w której wcześniej go zakupił. System powinien sprawdzić, że kupon faktycznie został wcześniej sprzedany w tej kolekturze (że gracz nie spreparował go sam) oraz że wygrana za kupon nie została już wcześniej wypłacona. W momencie odbierania wygranej gracz musi oddać kupon i nie może użyć go ponownie (system oznacza go jako zrealizowany). Jeśli gracz zrealizuje kupon przed nastąpieniem części losowań objętych kuponem, te losowania przepadają – gracz nie będzie mógł zgłosić się po nagrodę, nawet jeśli taka by mu przysługiwała. Wysokie nagrody (od 2280 zł za konkretny zakład w konkretnym losowaniu) są opodatkowane stawką 10% odprowadzaną do budżetu państwa (w momencie odbierania wygranej przez gracza). Niższe zaś są nieopodatkowane. Jeśli dany kupon upoważnia do odebrania więcej niż jednej nagrody – przykładowo jednej wysokiej (trafiona piątka zakładem nr 5 w losowaniu nr 3) oraz dwóch niskich (trafiona trójka zakładem nr 1 w losowaniu nr 3 oraz trafiona czwórka zakładem nr 5 w losowaniu nr 7) – to opodatkowaniu podlega tylko kwota wysokiej nagrody. Ze względu na regulacje o ochronie prywatności ani centrala, ani kolektury nie mogą przechowywać informacji o graczach. Gracz może w dowolnym momencie odebrać swoją nagrodę. Jeśli w którymś momencie zabraknie w centrali pieniędzy na wypłatę nagrody, to centrala powinna pobrać brakującą kwotę w formie subwencji z budżetu państwa.

### Losowanie
Reprezentuje pojedyncze oficjalne losowanie. Każde losowanie ma swój unikatowy numer porządkowy (liczony od 1) i przechowuje swój wynik w postaci 6 liczb trzymanych w porządku rosnącym. Każde losowanie można przekształcić na napis, obejmujący numer losowania oraz wyniki zawierające wyrównane do prawej (z dodatkową spacją dla liczb jednocyfrowych) wylosowane liczby (zakończone spacją), np.

Losowanie nr 6901
Wyniki:  5  8  9 28 31 47  

### Wysokość wygranych 
51% wszystkich wpłat z zakładów (pomniejszonych o kwotę podatku) w danym losowaniu przeznaczane jest na nagrody, a reszta to zysk centrali.

Kwota na nagrody dzielona jest następująco:

- 44% przeznaczane jest na nagrody I stopnia (trafione 6 liczb),
- 8% rezerwowane jest na nagrody II stopnia (trafione 5 liczb),
- każda nagroda IV stopnia (trafione 3 liczby) jest równa dokładnie 24,00 zł,
- reszta przeznaczana jest na nagrody III stopnia (trafione 4 liczby).
Pula na nagrody I stopnia jest gwarantowana i wynosi minimum 2 mln. zł (nie wpływa to na wielkości pul pozostałych stopni wyliczone wyżej). Każda z pul na nagrody I, II i III stopnia dzielona jest po równo między zakłady z odpowiednią liczbą trafień (a w zasadzie posiadających je graczy). Pojedyncza nagroda III stopnia nie może być niższa niż 15x stawki za jeden zakład (15 x 2,40 = 36,00 zł). Jeśli w losowaniu nie padnie wygrana I stopnia, to jej pula powiększa pulę na nagrody I stopnia w kolejnym losowaniu (kumulacja). Jeśli w centrali brakuje środków na nagrody, to korzysta ona z subwencji, w brakującej kwocie, z budżetu państwa.

Przy wszelkiego rodzaju obliczeniach procentowych oraz przy podziale puli między graczy, jeśli wychodzi niecałkowita liczba groszy, w tym zadaniu należy zaokrąglić w dół do pełnego grosza.

### Kolektura 
Kolektura jest miejscem, gdzie można kupić kupon, zawierający wytypowane liczby (pogrupowane w zakłady) i upoważniający do uczestnictwa w grze i ewentualnego odbioru wygranych. Nagroda za zwycięski kupon może być wypłacona jedynie w kolekturze, w której został on nabyty.

Każda kolektura ma unikatowy numer oraz przechowuje w swojej bazie wszystkie sprzedane przez siebie kupony. Kolektura wysyła wszystkie zyski natychmiast do centrali; kwoty na wypłaty nagród pobiera również z centrali. Kolektura może mieć własny bufor finansowy, ale w tym zadaniu go nie modelujemy.

W ofercie kolektury jest sprzedaż kuponów dwojakiego rodzaju – kuponów generowanych w oparciu o specjalny blankiet wypełniany przez klienta oraz kuponów na tzw. "chybił-trafił". W tym drugim przypadku do sprzedaży kuponu wystarczy podać liczbę zakładów i liczbę losowań. W pojedynczej transakcji sprzedaży można kupić tylko jeden kupon. Transakcja ta dochodzi do skutku tylko wtedy, gdy klient dysponuje środkami wystarczającymi na pokrycie ceny kuponu. Otrzymane środki ze sprzedaży, pomniejszone o podatek, trafiają do kasy Totolotka. Podatek trafia do budżetu państwa. Kupony są rejestrowane w bazie kolektury i przekazywane klientom tylko wtedy, gdy uiszczona została za nie zapłata.

### Blankiet 
Blankiet jest formularzem na podstawie którego kolektura może automatycznie wygenerować kupon dla klienta. Blankiet zawiera 8 ponumerowanych (od 1) pól reprezentujących poszczególne zakłady.

Pole zakładu zawiera 49 kratek, każda ponumerowana w środku liczbą z zakresu od 1 do 49, oraz jedną kratkę bez wypełnienia oznaczoną słowem "anuluj". Na pojedynczym blankiecie można zatem wytypować do 8 zakładów. Wytypowanie zakładu polega na wybraniu jednego z pól blankietu i skreśleniu na nim, poziomą kreską, wnętrza 6 kratek z liczbami. Liczba w skreślonej kratce jest tą, którą typujemy. Pole można anulować zaznaczając kratkę "anuluj". Zakład z takiego anulowanego pola nie będzie uwzględniony w wygenerowanym kuponie. Pole, na którym zaznaczono inną niż sześć liczbę kratek liczbowych, powinno być z automatu pomijane - nie trzeba go dodatkowo anulować.

Oprócz tego, na dole blankietu znajduje się dodatkowe 10 kratek z liczbami od 1 do 10 opatrzonych wyrażeniem "Liczba losowań:". Można zaznaczyć jedną z tych liczb, w celu wybrania liczby losowań, w których mają brać udział zakłady wytypowane na blankiecie. Dopuszczalne jest zostawienie tych kratek bez zaznaczenia, co oznacza wybór tylko jednego losowania. Jeśli zaznaczonych zostanie więcej tych kratek, to obowiązuje ta z największą liczbą.

### Kupon
Kupon jest dokumentem (małą karteczką) wystawianym przez kolekturę, który przekazywany jest klientowi po uiszczeniu zapłaty za wytypowane zakłady. Kupon jest jedynym dokumentem pozwalającym graczowi na odbiór ewentualnej wygranej. Blankiet, z którego ewentualnie wygenerowano zwycięski kupon, nie daje takiej możliwości.

Każdy kupon ma unikatowy (w skali całego systemu) numer porządkowy nadawany podczas tworzenia. Numeracja kuponów zaczyna się od 1. W oparciu o ten numer i numer kolektury, która wystawiła kupon, tworzony jest identyfikator kuponu. Identyfikator ten jest napisem zawierającym kolejno numer kuponu, numer kolektury, losowy znacznik i sumę kontrolną, które są oddzielone znakiem '-'. Losowy znacznik zawiera 9 losowych cyfr. Generowany jest on raz podczas tworzenia kuponu. Suma kontrolna jest równa sumie cyfr w numerze kuponu, numerze kolektury i w znaczniku losowym, modulo 100, zapisanej z ewentualnym zerem wiodącym. Przykładowy identyfikator kuponu: 1959-790-959497998-09.

Każdy kupon może zawierać od 1 do 8 zakładów, czyli zestawów 6 wytypowanych liczb. Ponadto może być zawarty na co najwyżej 10 kolejnych losowań, przy czym pierwszym losowaniem jest zawsze najbliższe losowanie. Klient płaci za kupon cenę równą cenie pojedynczego zakładu (w tym podatek), pomnożonych przez liczbę zakładów i liczbę losowań.

Kupon musi udostępniać publicznie informacje o swoim identyfikatorze, cenie i podatku jaki trafił do budżetu państwa. Wydruk kuponu powinien zawierać kolejno w wierszach: 
1. identyfikator kuponu,
2. ponumerowaną listę kolejnych zakładów (w osobnych wierszach, wylosowane liczby wyrównane do prawej),
3. liczbę losowań,
4. listę numerów losowań (w jednym wierszu),
5. cenę brutto kuponu.

### Gracz
Gracz jest osobą, która może w wybranej kolekturze kupować kupony. Każdy gracz ma imię, nazwisko i PESEL (nie trzeba sprawdzać poprawności PESELu) oraz dysponuje pewnymi środkami pieniężnymi. Przechowuje ponadto zakupione kupony.

Każdy gracz potrafi wypisać informacje o sobie, które zawierają: nazwisko, imię, PESEL, posiadane środki oraz listę identyfikatorów posiadanych kuponów lub informację o braku kuponów.

W Totolotka mogą grać gracze różnego rodzaju. Minimalista kupuje kupon zawsze w swojej ulubionej kolekturze. Obstawia wtedy tylko jeden zakład na chybił-trafił i tylko na jedno najbliższe losowanie. Gracz losowy w losowo wybranej kolekturze kupuje losową liczbę kuponów chybił-trafił (od 1 do 100 szt.); każdy kupon z losową liczbą zakładów i na losową liczbę losowań (w ramach ograniczeń kuponu). Gracz tego rodzaju dysponuje losowo wybraną początkową ilością środków (mniej od miliona zł). Gracz stałoliczbowy ma ulubione 6 liczb i za każdym razem wypełnia nowy blankiet obstawiający te liczby na 10 najbliższych losowań. Gracz ten kupuje nowy kupon dopiero wtedy, gdy przeprowadzone zostaną wszystkie losowania obstawione w poprzednim kuponie. Gracz stałoblankietowy ma swój blankiet i kupuje kupon zawsze w oparciu o ten blankiet, przy czym robi to co pewną stałą - sobie znaną - liczbę losowań. Każdy z graczy stałoliczbowych i stałoblankietowych ma kilka (jedną lub więcej) ulubionych kolektur, z których korzysta na zmianę po kolei (kolejny kupon kupuje w kolejnej ulubionej kolekturze; gdy odwiedzi wszystkie, zaczyna z powrotem od pierwszej).

System powinien mieć możliwość łatwego dodawania innych rodzajów graczy.

### Budżet Państwa
Pobiera podatki i przekazuje subwencje. Potrafi także wypisywać łączną kwotę pobranych podatków i łączną kwotę przekazanych subwencji.

Zakładamy, że budżet państwa jest nieporównywalnie większy niż kwoty, którymi operuje nasza loteria, i nie musimy modelować konkretnej kwoty, którą budżet dysponuje.

