## CENTRALA
#### atrybuty:
    - środkiFinansowe
    - listaZakładów
    - listaKolektur
    - wygrane I / II / III / IV
#### operacje:
    - skorzystajZSubwencji(long kwota)
    - przeprowadźLosowanie()
    - obliczPulęNagród()
    - obliczWygrane()
    - skumulujWygrane()
    - podajWyniki() {
        - kwoty wygrane dla każdego stopnia
        - liczba zwycięskich, zakładów dla wygranych każdego stopnia
        - łączna pula nagród każdego stopnia
    - podajStanFinansowy()

## KOLEKTURA
#### atrybuty:
    - numer
    - sprzedaneKupony
    
#### operacje:
    - zrealizujKupon(kupon)
    - przekażZyskiDoCentrali()
    - pobierzPieniądzeZCentrali()
    - sprzedajKupon()
        - z blankietu
        - chybił-trafił
            - podatek do budżetu państwa, reszta do kasy totolotka 
    - 

## GRACZ
###### ***klasa abstrakcyjna***

#### atrybuty wspólne:
    - imię
    - nazwisko
    - PESEL
    - środkiFinansowe

#### podklasy:
1. [Minimalista](#minimalista)
2. [Losowy](#losowy)
3. [Stałoliczbowy](#stałoliczbowy)
4. [Stałoblankietowy](#stałoblankietowy)


### Minimalista
#### atrybuty:
    - ulubionaKolektura

##### zachowanie:
obstawia jeden zakład na chybił-trafił i na jedno, najbliższe losowanie


### Losowy
#### atrybuty:

##### zachowanie:
w losowo wybranej kolekturze kupuje losową liczbę kuponów (od 1 do 100 sztuk); każdy kupon z losową liczbą zakładów i na
losową liczbę losowań (w ramach ograniczeń kuponu). 
Początkowo ma losową ilość środków (< 1 000 000 zł)


### Stałoliczbowy
#### atrybuty:
    - ulubioneLiczby
    - ulubioneKolektury

##### zachowanie:
przy każdym wypełnieniu blankietu obstawia ulubione liczby na kolejne **10** losowań. 
Kupuje nowy kupon dopiero wtedy, gdy przeprowadzone zostaną wszystkie losowania obstawione w poprzednim kuponie. 


### Stałoblankietowy
#### atrybuty:
    - blankiet
    - coIleLosowań
    - ulubioneKolektury
#### zachowanie:
zawsze kupuje kupon w oparciu o swój blankiet, co pewną stałą liczbę losowań
    
***Po kolei korzysta ze swoich ulubionych kolektur (dotyczy stałoliczbowego i stałoblankietowego). Gdy odwiedzi 
wszsytkie, zaczyna od nowa od pierwszej***

### oepracje:
    - odbierzWygraną()
    - 


## KUPON
#### atrybuty:
    - gdzieKupiony - nr kolektury
    - czyZrealizowany
    - numerPorządkowy
    - identyfikatorKuponu
    - cena = 1 zakład * liczba zakładów * liczba losowań
    - ileZakładów
    - ileLosowań
    - 


## LOSOWANIE
reprezentuje jedno, oficjalne losowanie
#### atrybuty:
    - numerPorządkowy
    - wynikLosowania - 6 liczb w porządku rosnącym 
#### operacje:
    - wypiszLosowanie()


## NAGRODY
IV - 24 zł
III - pozostała kwota
II - 8% puli
I - 44% puli - gwarantowane min 2 mln + ew. kumulacja

## BLANKIET
#### atrybuty:
    - ileWytypowanychZakładów
    - 

## ZAKŁAD
#### atrybuty:
    - zaznaczoneLiczby
    - czyAnulowany
    - liczbaLosowań
#### operacje:
    - anulujZakład()
    - sprawdźLosowania()


## BUDŻET PAŃSTWA   
#### atrybuty: 
    - pobranePodatki
    - przekazaneSubwencje
#### operacje:
    