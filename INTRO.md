# Kaiser – Spielanleitung

Du regierst die antike Stadt **Sumeria** für zehn Jahre. Deine Entscheidungen
bestimmen, ob die Bevölkerung ausreichend Nahrung hat und die Stadt gedeiht.
Nach deiner Amtszeit wird bewertet, wie viele Menschen verhungert sind und wie
viel Land pro Einwohner vorhanden ist.

## Start und Eingaben

Starte das Spiel mit `./run.sh` und bestätige `Start (J/N)?` mit `j`.
Du beantwortest anschließend die Fragen der Reihe nach mit Zahlen und bestätigst
jede Eingabe mit Enter.

- **Land** wird in **Hektar** angegeben.
- **Getreide** wird in **dt** angegeben: Eine Dezitonne entspricht 100 kg.
- `0` überspringt einen Kauf oder Verkauf. Bei Nahrung und Anbau bedeutet `0`
  tatsächlich: nichts verteilen beziehungsweise nichts anbauen.
- **Negative Mengen beenden das Spiel.**
- Dezimalzahlen sind möglich; verwende einen Punkt statt eines Kommas.
- Bei ungültigen Zahlen oder Mengen, die deine Ressourcen überschreiten, wirst
  du erneut gefragt.

## Dein Ausgangspunkt

In der ersten Runde hast du:

| Ressource | Menge |
| --- | ---: |
| Einwohner | 100 |
| Land | 1000 Hektar |
| Getreidevorrat | 2800 dt |

**Jeder Einwohner benötigt 20 dt Nahrung pro Jahr.** Für die anfänglichen
100 Einwohner musst du also **2000 dt** verteilen – nicht `100`!

## So läuft ein Jahr ab

Zu Jahresbeginn erhältst du einen Bericht über die bisherige Entwicklung:
Hungertote, Zuwanderung, Bevölkerung, Land, letzte Ernte, Rattenverluste und
Getreidevorräte. Zuwanderer und eine mögliche Seuche sind in der angezeigten
Einwohnerzahl bereits berücksichtigt. Eine Seuche halbiert die Bevölkerung.

Danach entscheidest du in dieser Reihenfolge:

### 1. Land kaufen oder verkaufen

Der Preis wird jedes Jahr neu bestimmt und beträgt **17 bis 26 dt pro Hektar**.
Käufe werden mit Getreide bezahlt; Verkäufe bringen Getreide ein.

Wenn du beim Kauf `0` eingibst, wirst du nach einem Verkauf gefragt. Nach einem
positiven Kauf gibt es in diesem Jahr keinen Verkauf. Du darfst nicht dein
gesamtes Land verkaufen.

**Kaufe nicht so viel Land, dass anschließend Nahrung und Saatgut fehlen.**

### 2. Nahrung verteilen

Gib die **Getreidemenge** ein, die deine Bevölkerung dieses Jahr erhalten soll.
Als Faustregel gilt:

**Benötigte Nahrung = angezeigte Einwohnerzahl × 20 dt**

Nur vollständige Portionen von 20 dt ernähren einen Einwohner. Bei `100` dt
werden beispielsweise nur fünf Menschen versorgt. Zusätzlich verteiltes
Getreide erhöht die Bevölkerung nicht.

Die Nahrung wird sofort von deinem Vorrat abgezogen. **Die kommende Ernte hilft
nicht mehr bei der Versorgung dieses Jahres.** Sie steht erst für die nächste
Runde zur Verfügung.

### 3. Land bebauen

Gib die Fläche in Hektar ein, die du bestellen möchtest. Dabei gelten drei Grenzen:

- Du kannst höchstens so viel Land bebauen, wie du besitzt.
- Als Saatgut wird die Hälfte der Fläche in dt benötigt, auf eine ganze dt
  abgerundet. Danach muss noch ein positiver Getreiderest übrig sein.
- Du musst **weniger als zehn Hektar pro Einwohner** bebauen. Bei 100 Einwohnern
  sind also `1000` Hektar nicht erlaubt, `999` hingegen schon – sofern Land und
  Saatgut reichen.

Die Ernte bringt zufällig **1 bis 5 dt pro bebautem Hektar**. Ratten können einen
Teil des nach der Aussaat verbleibenden Vorrats fressen; anschließend kommt die
neue Ernte hinzu. Bei `0` Hektar gibt es keine Ernte, mögliche Rattenverluste und
die übrigen Jahresereignisse finden trotzdem statt.

### 4. Folgen deiner Entscheidungen

Am Jahresende wird berechnet, wie viele Einwohner deine zugeteilte Nahrung
versorgen konnte. **Verhungern mehr als 45 % der Bevölkerung in einem Jahr,
wirst du sofort abgesetzt.** Es müssen also nicht alle Einwohner verhungern,
damit das Spiel endet.

Wenn du im Amt bleibst, beginnt das nächste Jahr mit den verbleibenden Einwohnern,
Zuwanderern und einer möglichen Seuche. Berechne den Nahrungsbedarf deshalb in
jeder Runde anhand der neuen Einwohnerzahl.

## Beispiel für die erste Runde

Mit diesen Eingaben ernährst du alle Einwohner und baust Getreide an:

| Frage | Eingabe | Bedeutung |
| --- | ---: | --- |
| Start (J/N)? | `j` | Spiel beginnen |
| Land kaufen? | `0` | Kein Kauf |
| Land verkaufen? | `0` | Kein Verkauf |
| Getreide an das Volk verteilen? | `2000` | Nahrung für 100 Einwohner |
| Land bebauen? | `900` | 900 Hektar bestellen |

Nach der Versorgung bleiben 800 dt, nach dem Saatgutverbrauch 350 dt.
Die Ernte beträgt dann je nach Zufall 900 bis 4500 dt; Ratten können den
verbleibenden Vorrat von 350 dt noch verringern. Dieses Beispiel verhindert
Hungertote **in der ersten Runde**,
ist aber keine Garantie für einen erfolgreichen weiteren Spielverlauf.

## Tipps und Spielende

- Plane zuerst Nahrung, dann Saatgut und erst danach zusätzliche Landkäufe.
- Bei knappen Vorräten kann ein Landverkauf die Versorgung sichern.
- Baue ausreichend Getreide für das nächste Jahr an und rechne mit schlechten
  Ernten, Rattenverlusten und wachsender Bevölkerung.
- Behalte sowohl die Versorgung als auch das Land pro Einwohner im Blick:
  Beides zählt für die Abschlussbewertung.

Nach zehn Entscheidungsrunden folgt der Bericht für **Jahr 11**, aber keine weitere
Kauf-, Versorgungs- oder Anbaurunde. Die beste Bewertung erhältst du bei höchstens
3 % durchschnittlichen Hungertoten pro Jahr und mindestens zehn Hektar Land pro
Einwohner. Nicht jede Abschlussbewertung bietet einen Neustart an; gegebenenfalls
startest du das Programm erneut.

Die genauen Regeln und Besonderheiten des BASIC-Originals sind in
[BASIC-PORT.md](./BASIC-PORT.md) dokumentiert.
