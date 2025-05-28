## CENTRALA
#### atrybuty:
    - środki finansowe
    - lista zakładów
    - lista kolektur
    - wygrane I / II / III / IV
#### operacje:
    - skorzystajZSubwencji(long kwota)
    - przeprowadźLosowanie()
    - obliczPulęNagród()
    - obliczWygrane()
    - skumulujWygrane()
    - podajWyniki() {
        - kwoty wygrane dla każdego stopnia
        - liczba zwycięskich zakładów dla wygranych każdego stopnia
        - łączna pula nagród każdego stopnia
    - podajStanFinansowy()

## GRACZ
###### ***klasa abstrakcyjna***

#### atrybuty wspólne:
    - imię
    - nazwisko
    - PESEL
    - środki finansowe

#### podklasy:
    1. [Go to Minimalista](#centrala)
    2. Losowy
    3. Stałoliczbowy
    4. Stałoblankietowy

### Minimalista