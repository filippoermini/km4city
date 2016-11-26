# km4city
Documentazione per il file xml di generazione delle triple.
## tree 
nodo radice

propriertà:
- typeId: valore dell'URI dell' *rdf-syntax-ns#type*
- simulationName: nome della simulazione

elementi:
- classIterationQuery: elemento di tipo *queryInfo* contenete la query rdf con cui fare iterare le classi definite
- fileInfo: elemento contenente i campi per la definizione del file .n3 da generare
- iterationelement: contiene una lista di classi e di attributi da generare ad ogni iterazione

## fileInfo
contiene i campi per la definizione del file .n3 da generare.

proprietà: nessuna

elementi:
- fileName: nome del file .n3 da generare
- startDirectory: path della cartella in cui creare il file .n3
## queryInfo
elemento per la definizione di una query SPARQL

proprietà: nessuna

elementi:
- query: query SPARQL
- servar: end point a cui sottoporre la query
- bindingValue: campo su cui andare a rcuperare il valore (dafault = id)
## iterationElement
contiene gli elementi che devono essere generati ad ogni iterazione contiene una lista (opzionale) 
di attributi il cui valore sarà necessario ai fini del calcolo dei valori degli attibuti delle classi ed una lista di classi 
rappresentanti le classi rdf e di loro attributi che andranno a comporre il le triple rdf del file .n3 .

proprietà: nessuna

elementi:
- attributes: lista di elementi di tipo *properties* contnente una lista di attributi 
- list\<class\>: lista di classi rdf da generare

## class
elemnto che definisce la struttura e gli attributi di una classe rdf.

proprietà:
- type: valore dell'uri *rdf-syntax-ns#type* della classe
- name: nome della classe usato per referenziare la classe 
- isRoot: se *true* indica se questa classe contiene l'emento su cui inserire il valore generato dalla query
- baseUri: valore del base URI della classe

elementi:
- properties: elemento contenente una lista di attributi

## properties
contiene la lista delle proprietà della classe. questo elemento è usato anche per definire gli attributi generrali dell'iterazione.

proprietà: nessuna

elementi:
- list\<prop\>: contiene una lista di proprietà che definiscono ognua una tripla rdf

## prop
definisce le singole proprietà della classe 

proprietà: 
- key: valore dell'URI della proprietà
- name: nome della proprietà, utilizzato per referenziare la proprieta 

elementi:
- type: indica il tipo di valore che deve essere generato (ogni tipologia può necessitare alementi aggiuntivi).
- uri: valore dell'*oggetto* della tropla rdf

type value:

tip | Descrizione | Campi aggiuntivi |
------------ | ------------- | --------------
id | definische che la proprietà dovrà contenere il valore proveniente dall query di iterazione, questa proprietà deve essere attribuita alla classe con isRoot = true | nessuno
integer | l'attributo restituirà un valore intero generato casualmente tra due valori | **maxValue**,**minValue** estremi dell'intervallo 
uuid | genera un identificativo univoco | nessuno
float | l'attributo restituirà un valore float generato casualmente tra due valori | **maxValue**,**minValue** estremi dell'intervallo 
md5  |  genera il valore attraverso la funzione md5 di una stringa | **md5String** contiene la stringa da crittografare 
datetime  | genera una data in formato ISO 8601 se non specificati gli estremi la date assume il valore dell'istante in cui è generata |  **maxValue**,**minValue** estremi dell'intervallo per la generazione di una data casuale
hourdependent |  serve a generare dei valori diversi per ogni ora della giornata in base a valori orari di riferimento |  **hourValue**: contiene i valori di riferimento per ogni ora, devono essere inseriti 24 valori separati da ';' . **range**: contiene il range di variazione all'interno del quale possiamo far variare il valore di riferimento, può essere un valore unico per tutti e 24 i valori oppure può essere definito in modo analogo a **hourValue** indicando i 24 valori del range
fromset  |  estrae un valore a caso da un set di elementi (se il set contiene un solo elemento verrà estratto sempre quello) | **set** deve contenere la lista di elementi separati da';'
query  | il valore dell'attributo è il risultato dell'esecuzione di una query, la query deve restituire un solo valore |  *elemento di tipo queryInfo rifarsi alla definizione dello stesso*
valueexpression | il valore dell'attributo è ottenuto dall'esecuzione di una espressione che può contenere variabili, le variabili si riferiscono ai valori assunti da altre propiertà | **valueExpression* contiene l'espressione algebrica o logica da eseguire, le variabile devono essere dichiarate con la seguente sintassi ${nome} dove nome è il valore inserito nelle proprietà dell'attributo *prop*

le espressione logiche ed algebriche oltre che nelle proprietà in cui sono definite possono essere inserite nei campi aggiuntivi di altre proprietà
ad esemprio nei campi maxValue o minValue oppure all'interno di una query.
datetime