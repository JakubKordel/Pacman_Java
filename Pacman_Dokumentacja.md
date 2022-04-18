**Projekt PROZ Jakub Kordel Gra Pacman**



1. Użyte narzędzia

   W celu narysowania gry i obsługi wejścia z klawiatury/myszy wykorzystałem głównie biblioteki awt oraz swing. 

   W projekcie używałem również klasy Vector2f z zewnętrznej biblioteki Slick (org.newdawn.slick.geom.Vector2f).

   Przy pisaniu testów korzystałem z biblioteki JUnit.

   Ostateczny plik z grą Pacman.jar został wygenerowany w środowisku Intelij Idea w systemie operacyjnym Linux Debian. Gra uruchomiła się  poprawnie również w środowisku systemu windows. 

   

   2. Struktura projektu

      Kod podzieliłem na cztery główne moduły *model*, *controller*, *view* oraz *tests*. 

      

      Moduł model:

      

      W tym miejscu zamodelowałem podstawowe elementy gry takie jak schemat mapy (korytarzy), poprawne poruszanie się obiektów po mapie (w tym przechodzenie przez portale) czy jedzenie do zjedzenia przez gracza. 

      Moduł podzieliłem na trzy foldery/paczki: arena, movingObjects, staticObjects.

      W celu wytłumaczenia sposobu implementacji planszy posłużę się 'kartką w kratkę'.  Kartka w kratkę ma regularnie ułożone punkty przecięcia się prostopadłych linii. Przecinające się linie tworzą siatkę. Każdy punkt przecięcia opisany jest programie przez dwie liczby całkowite, identyfikujący położenie punktu w całej siatce. W programie punkty przecięcia na siatce reprezentuje klasa Node z paczki model.arena. Wszystkie nieruchome obiekty (Apple, Orange, Portal z paczki model.staticObjects) znajdują się w punktach przecięcia siatki i dziedziczą po klasie Node. Implementacja polega na zapewnieniu poruszania się obiektów tylko po liniach na siatce.

      Obiekt klasy Node zawiera informacje o tym, w których kierunkach możliwe jest opuszczenie tego punku przez poruszający się obiekt. Metoda trigger(MovingObject movingObject) przeznaczona jest do wywołania przez każdy poruszający się obiekt który trafi do 'węzła'. Zachowanie metody trigger zależy od typu węzła np. dla portalu jest to zmiana położenia poruszającego się obiektu.

      Informacje o typie wezłów na siatce można w łatwy sposób przechowywać w dwuwymiarowej tablicy. Definicje planszy (wezłów) umieściłem w klasie MapScheme. Definicja polega na wypełnieniu dwuwymiarowej tablicy intów liczbami symbolizującymi rózny typ węzła. Np. 0 to brak ścieżki w danym miejscu, 3 symbolizuje zwykłą pustą ścieżkę a 1 pomarańczę. Takie rozwiązanie ułatwia projektowanie planszy ponieważ na taką tablicę łatwiej się patrzy niż na tablicę wypełnioną napisami typu "new Orange(...)". Konwersji na tablicę obiektów typu Node (lub dziedziczących po Node) dokonuje konstruktor klasy Map który jako parametr przyjmuje obiekt klasy MapScheme. W klasie MapScheme umieściłem również definicję ścian alejek. Definicja ta ma jednak znaczenie tylko przy wyświetlaniu i wykorzystywana jest w module view.   Konstruktor klasy Map wypełnia tablicę obiektów typu Node z poprawnie zainicjalizowaną informacją o dopuszczalnych kierunkach wychodzących z węzła (na podstawie sąsiadów). W miejscach zer ze schematu nowa tablica ma wartość null. Korzystając z definicji z obiektu typu MapScheme konstruktor klasy Map 'łaczy' również portale. 

      Mając gotową tablicę węzłów w obiekcie klasy Map, mogę przejść do omówienia implementacji najważniejszej klasy MovingObject. Klasa jest klasą abstrakcyjną i przeznaczona jest do dziedziczenia przez obiekty reprezentujące gracza (Pacmana) i duszki. 

      

      Metoda move wogóle nie daje gwarancji, że wykonany przez nią ruch będzię 'legalny' tzn, że obiekt pozostanie w alejce. Zadaniem metody move jest wywoływanie abstrakcyjnej metody Direction fetchNextDirection() podczas gdy obiekt mija Node(węzeł/skrzyżowanie). Metody move 'nie interesuje' czy pobrany nowy kierunek przed metodę fetchNextDirection() spowoduje wyjście obiektu z alejek. Może obiekt posiada moc przechodzenia przez ścianę? To obiekty dziedziczące po klasie MovingObject zapewnią, że metoda fetchNextDirection zwróci dopuszczalny kierunek. Klasa MovingObject dostarcza metodę isMoveAvailable(Direction d) w celu udostępnienia łatwej możliwości sprawdzenia czy dany kierunek dla danego obiektu jest dopuszczalny tzn. czy obiekt nie opuści alejek.  

      

      Jeżeli obiekt się nie porusza to należy dać mu możliwość rozpoczęcia poruszania. Należy również sprawdzić czy poruszający obiekt nie zarządał odwrócenia kierunku na przeciwny, bowiem gracz powinien mieć w dowolnym momencie możliwość zmiany kierunku na przciwny a nie tylko w momencie gdy trafia w węzęł (Node). Zmiana kierunku poruszania się na przeciwny jest zawsze 'legalna'  i nigdy nie doprowadzi do wyjścia z alejek. 

      ```
      public void move(){
              if (currentDirection == Direction.NONE){
                  currentDirection = fetchNextDirection();
              } else if(reverseWanted){
                  reverseWanted = false;
                  currentDirection = Direction.opposite(currentDirection);
              }
      ```

      

      Teraz należy przemieścić obiektu o wartość 'speed' w danym kierunku. Jeżeli obiekt po przemieszczeniu nie minął węzła (współrzędne obiektu float nie zmieniły części całkowitej) to oznacza to koniec procedury ruchu.
      
      ```
       		Vector2f prevPos = new Vector2f(pos);

 		pos.add(getSpeed(currentDirection, speed));
 	  ```


​      
​      
      Jeżeli obiekt minął węzeł, to należy go cofnąć do miniętego węzła, dać możliwość wyboru nowego kierunku i dodać wartość o którą cofnięto obiekt w nowym kierunku. Obiekt odwiedził węzeł, więc należy również wywołać metode trigger w danym węźle.
      
      ```
      		if((int)prevPos.x != (int)pos.x || (int)prevPos.y != (int)pos.y){
                  float speedRemainder;
                  switch (currentDirection) {
                      case UP:
                          speedRemainder = ((float) (int)prevPos.y) - pos.y;
                          pos.y = (float) (int)prevPos.y;
                          break;
                      case DOWN:
                          speedRemainder = pos.y - ((float) (int) pos.y);
                          pos.y = (float) (int)pos.y;
                          break;
                      case LEFT:
                          speedRemainder = ((float) (int)prevPos.x) - pos.x;
                          pos.x = (float) (int)prevPos.x;
                          break;
                      case RIGHT:
                          speedRemainder = pos.x - ((float) (int) pos.x);
                          pos.x = (float) (int)pos.x;
                          break;
                      default:
                          speedRemainder = 0;
                          break;
                  }
                  map.node[(int)pos.x][(int)pos.y].trigger(this);
                  currentDirection = fetchNextDirection();
                  if (speedRemainder == 0){ //unlikely case, but possible
                      speedRemainder = 0.00001f;
                  }
                  if (!isMoveAvailable(currentDirection)){
                      currentDirection = Direction.NONE;
            }
                  pos.add(getSpeed(currentDirection, speedRemainder));
        }
      ```


​      
​      
      Implementacja gracza z dziedziczeniem po klasie MovingObject jest bardzo prosta. Implementacja polega na implementacji metody fetchNextDirection(). Należy tylko sprawdzić, czy wybrany przez gracza kierunek jest dopuszczalny. Jeśli nie to obiekt porusza się tak jak wcześniej. 
      
      ```
      @Override
          public Direction fetchNextDirection() {
              if (isMoveAvailable(wantedDirection))
                  return wantedDirection;
              else if (isMoveAvailable(currentDirection))
                  return currentDirection;
              else
                  return Direction.NONE;
          }
      
    public void setWantedDirection(Direction direction){
              wantedDirection = direction;
    }
      ```
    
      Metoda setWantedDirection przeznaczona jest do użycia w listenerze klawiatury. 


​      
​      
      Wszystkie duszki mają wiele wspólnego dlatego napisałem klase abstrakcyjną Ghost dziedziczącą po MovingObject. Duszki mogą być w trzech stanach: DEFAULT, HUNT i ESCAPE. W zależności od stanu przyjmują różną strategię poruszania. Każdy duszek może zachowywać się inaczej, zatem klasa definiuje trzy metody abstrakcyjne:
      
      ```
abstract Direction defaultMove();
      abstract Direction huntMove();
abstract Direction escapeMove();
      ```

​      

      W klasie tej umieściłem też klasę zagnieżdzoną Strategy która zawiera tablicę definiującą strategię ruchu. Metoda symmetricalPriorityStrategy(Strategy strategy, boolean reverse) ma za zadanie zwrócić najbardziej preferowany i dopuszczalny kierunek według zdefiniowanej strategii. Sposób definicji zakłada, że strategia jest symetryczna w każdym kierunku, dlatego w tablicy definiuje się strategie dla sytuacji gdy gracz jest dokładnie nad duszkiem lub na północny wschód od duszka. Pozostałe możliwości są tylko rotacją tych dwóch sytuacji i metoda symmetricalPriorityStrategy rozwiązuje je analogicznie. Gracz może się poruszać w czterech kierunkach lub nie poruszać się wogóle zatem jest 10 sytuacji do zdefiniowania. Dla każdej z 10 sytuacji należy podać preferencje następnego ruchu duszka (ustawić w kolejności). Założyłem, że wszystkie cztery kierunki mogą mieć ten sam poziom preferencji zatem ilość pół w tablicy w sumie wynosi aż 160. należy jednak wypełnić tylko 140 pół. Dokładny opis przeznaczenia pół znajduje się przy klasie Strategy a przykłady wypełnionych definicji w klasach z duszkami. (klasy Blinky, Inky, Pinky, Inky).


​      

      Moduł controller:


​      
​      
      W tym module umieściłem kod związany z obsługą wejścia. W klasie KeyEventListener znajduje się kod dotyczący obsługi zdarzeń z klawiatury a w pliku MouseInput.java umieściłem listener zdarzeń dotyczących myszy. Trzecim plikiem z tego modułu jest PacmanEngine.java. Jest to główna klasa przechowywująca informację o aktualnym stanie gry. Klasa ta udostępnia metodę update(), której zadaniem jest uaktualnienie stanu gry. Użytkownik może być w jednym z trzech głównych 'okienek':  oknie Menu, w oknie gry lub w oknie z isntrukcją.  Informacje o aktualnie 'otwartym' oknie przechowuje zmienna window. Sama gra może być natomiast w większej liczbie stanów. Wyodrębniłem siedem stanów: 
    
      ```
public enum GameState { BEFOREROUND, RANDOM, HUNT, ESCAPE, LOST, WON, LIFELOST };
      ```

      Zachowanie metody update() zależy głównie od wartości zmiennej state typu GameState.
    
      W stanie ESCAPE duszki mogą zostać zjedzone przez gracza i powinny uciekać przed graczem. W stanie RANDOM duszki wykonują ich ruch zwykły/domyślny. W stanie HUNT duszki obierają odpowiednią strategie w celu złapania gracza. Stan WON to stan przed rozpoczęciem następnego poziomu gry (po tym jak gracz zjadł całe jedzanie z mapy). Stan LOST to stan w którym gracz stracił swoje trzy życia. W tym stanie gracz może przejść do menu (używając klawisza esc) i uruchomić  nową grę klikając przycisk Play. W menu gracz ma również do dyspozycji przyciski Help i Quit. 


​      

      Moduł view:
    
      W module view umieściłem kod rysujący gre, menu i stronę z instrukcją. Rysuję w klasie GamePanel dziedziczącej po JPanel. W metodzie run z tej klasy znajduje się główna pętla gry. W każdym obiegu tej pętli wywoływana jest funkcja repaint() która aktualizuje widok obiektów oraz metoda update() z klasy PacmanEngine, której zadaniem jest zaaktualizowanie modelu/stanu gry. Pozycje obiektów z modelu są skalowane przez stałą sizeMultiplier. Klasa GameMainFrame implementuje inicjalizacje okna gry oraz dołączenie do niego GamePanel i słuchaczów.   


​      

      W module Tests umieściłem testy obiektów z modelu, które są podstawową częścią gry. Testy uruchamiałem w środowisku Intelij. 


​      

      3. Uruchamianie:
    
         W celu uruchomienia gry należy przejść do katalogu z plikiem Pacman.jar i wykonać polecenie *java -jar Pacman.jar* lub np. uruchomić przy pomocy programu Java Oracle klikając prawym przyciskiem myszy na Pacman.jar.


​      
