


class Scene(object):

	global_index = 1;

	def __init__(self):
		self.index = Scene.global_index;
		Scene.global_index += 1;
		self.roles = [];

	def addRole(self, r):
		self.roles.append(r);


class Role(object):

	global_index = 1;

	def __str__(self):
		print("[", end="");
		for a in self.actors:
			print(a.index, end=" ");
		print("]");

	def __init__(self):
		self.actors = [];
		self.index = Role.global_index;
		Role.global_index += 1;

	def addActors(self, a):
		self.actors.append(a);


class Actor(object):

	global_index = 1;

	def __init__(self):
		self.index = Actor.global_index;
		Actor.global_index += 1;




class Graph(object):

	def __init__(self):
		self.edges = [];
		self.vertices = dict();


	def addVertex(self, vertex):
		self.vertices[vertex.index] = vertex;

	def addEdge(self, v1, v2):
		vert1 = self.vertices.get(v1);
		vert2 = self.vertices.get(v2);

		e = Edge(vert1, vert2);
		vert1.addEdge(e);
		vert2.addEdge(e);
		self.edges.append(e);


class Edge(object):

	def __init__(self, v1, v2):
		self.v1 = v1;
		self.v2 = v2;




class Vertex(object):

	def __init__(self, index):
		self.edges = [];		
		self.index = index;

	def addEdge(self, edge):
		self.edges.append(edge);



## Print base case
def print_base():
	print(3)
	print(2)
	print(3)
	print("1 1")
	print("1 2")
	print("1 3")
	print("2 1 3")
	print("2 2 3")

## Read graph coloring data

v = int(input());
e = int(input());
m = int(input());

if (m > v):
	print_base()
	exit()
	
g = Graph();

for i in range(v):
	v = Vertex(i+1);
	g.addVertex(v);


for i in range(e):
	line = input().strip().split(" ");
	v1 = int(line[0]);
	v2 = int(line[1]);

	g.addEdge(v1,v2);


## Set up RB

scenes = [];
roles = [];
actors = [];

d1 = Actor();
d2 = Actor();

actors.append(d1);
actors.append(d2);

rd1 = Role();
rd2 = Role();

rd1.addActors(d1);
rd2.addActors(d2);

sd1 = Scene();
sd2 = Scene();

scenes.append(sd1);
scenes.append(sd2);

roles.append(rd1);
roles.append(rd2);

sd1.addRole(rd1);
sd2.addRole(rd2);

for v in g.vertices:
	r = Role();

	roles.append(r);

	if(len(g.vertices[v].edges) == 0):
		s = Scene();
		s.addRole(rd1);
		s.addRole(r);
		scenes.append(s);

for edge in g.edges:
	s = Scene();
	r1 = roles[edge.v1.index-1+2];
	r2 = roles[edge.v2.index-1+2];
	s.addRole(r1);
	s.addRole(r2);

	scenes.append(s);

for c in range(m):
	a = Actor();
	for k in range(2,len(roles)):
		roles[k].addActors(a);

	actors.append(a);

sd1.addRole(roles[2]);
sd2.addRole(roles[2]);

## Print the problem to kattis 

print(len(roles));
print(len(scenes));
print(len(actors));

for i in range(len(roles)):
	r = roles[i];
	print(len(r.actors), end=" ");
	for a in r.actors:
		print(a.index, end=" ");
	print();

for i in range(len(scenes)):
	s = scenes[i];
	print(len(s.roles), end=" ");
	for r in s.roles:
		print(r.index, end=" ");
	print();






