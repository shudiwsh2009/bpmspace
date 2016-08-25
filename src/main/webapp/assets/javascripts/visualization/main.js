var sentence = [
{id: "0", form: "0"},
{id: "1", form: "nullum", lemma: "nullus1", postag: "a-s---ma-", head: "3", relation: "ATR"},
{id: "2", form: "esse", lemma: "sum1", postag: "v--pna---", head: "0", relation: "ExD"},
{id: "3", form: "aditum", lemma: "aditus2", postag: "n-s---ma-", head: "2", relation: "SBJ"},
{id: "4", form: "ad", lemma: "ad1", postag: "r--------", head: "3", relation: "AuxP"},
{id: "5", form: "eos", lemma: "is1", postag: "p-p---ma-", head: "4", relation: "ATR"},
{id: "6", form: "mercatoribus", lemma: "mercator1", postag: "n-p---md-", head: "2", relation: "ADV"},
];
var g = new dagreD3.Digraph();

jQuery.each(sentence, function(index, word) {
  g.addNode(word.id, { label: word.form });
});

jQuery.each(sentence, function(index, word) {
  if (word.head) {
    g.addEdge(word.id, word.id, word.head, { label: word.relation });
  }
});

var layout = dagreD3.layout().rankDir("BT");
var renderer = new dagreD3.Renderer();

vis = d3.select("svg g");
renderer.layout(layout).run(g, vis);
var start = null;
nodes = vis.selectAll("g.node").on("click", function(d, i) {
  var item = d3.select(this);
  if (start) {
    g.delEdge(start);
    g.addEdge(start, start, d);
    renderer.run(g, vis);
    start = null;
  }
  else {
    start = d;
  }

  var classes = item.attr("class");
  if (classes.indexOf(" selected") > 0) {
    item.attr("class", classes.replace(" selected", ""));
  }
  else {
    item.attr("class", classes + " selected");
  }
});
