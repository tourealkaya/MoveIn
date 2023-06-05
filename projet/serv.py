<<<<<<< HEAD
import socket
import numpy as np
import json

def Getdata(path):
    with open(path) as f:
        salles = {}
        noeuds = {}
        chemins = []
        # relations = []
        while True:
            line = f.readline()
            if (not line) or (line.strip() == '------------------'):
                break
            line = line.strip()
            data = line.split(",")
            salles[data[0]] = [data[1], data[2]]
        while True:
            line = f.readline()
            if (not line) or (line.strip() == '------------------'):
                break
            line = line.strip()
            data = line.split(",")
            noeuds[data[0]] = [data[1], data[2]]
        while True:
            line = f.readline()

            if (not line) or (line.strip() == '------------------'):
                break
            line = line.strip()
            data = line.split(",")

            chemins.append([data[0], data[1], data[2]])
    return {"salles": salles, "noeuds": noeuds, "chemins": chemins}








import heapq


def dijkstra(graph, start, end):
    # Initialisation
    print("start,end",start,end)
    distances = {node: float('inf') for node in graph}
    distances[start] = 0
    queue = [(0, start, [])]

    while queue:
        # On récupère le noeud avec la plus petite distance
        (cost, node, path) = heapq.heappop(queue)

        # Si on atteint le noeud d'arrivée, on renvoie le chemin
        if node == end:
            return path + [node]

        # Sinon, on continue à explorer les voisins
        for neighbor, weight in graph[node].items():
            new_cost = cost + weight
            if new_cost < distances[neighbor]:
                distances[neighbor] = new_cost
                new_path = path + [node]
                heapq.heappush(queue, (new_cost, neighbor, new_path))

    # Si on ne peut pas atteindre le noeud d'arrivée, on renvoie None
    return None

def deriv(tab):
    for i in range(len(tab)) :
        try :
            if(len(tab[i]) == 1) :
                rem = []
                for j in tab[i+1:]:
                    print(j)
                    if (len(j) == 1) :
                        for z in rem:
                            tab.remove(z)
                            break
                    rem.append(j)
                print(rem)
        except :
            break
    return tab
def tab2req(data,tab):
    salles = data["salles"]
    noeuds = data["noeuds"]
    salles.update(noeuds)
    final = ""
    for i in tab :
        final += str(i)+','+salles[i][0]+','+salles[i][1]+'|'
    return final

# Graphe de test
data = Getdata("PLAN.txt")
edges = data["chemins"]

# Conversion du graphe en dictionnaire
graph = {}
for edge in edges:
    node1 = edge[0]
    node2 = edge[1]
    weight = int(float(edge[2]))

    if node1 not in graph:
        graph[node1] = {}
    if node2 not in graph:
        graph[node2] = {}

    graph[node1][node2] = weight
    graph[node2][node1] = weight


print(graph)
# Calcul des distances les plus courtes
"""
distances = dijkstra(graph, s,end)
# Affichage des distances
print(distances)
print(deriv(distances))
"""
import socket
from threading import Thread
from socketserver import ThreadingMixIn


class myThread(Thread):
    def __init__(self, ip, port):
        Thread.__init__(self)
        self.ip = ip
        self.port = port
        print("[+] Nouveau thread démarré pour " + ip + ":" + str(port))

    def run(self):
        while True:

            try :
                d = con.recv(2048)
                d = d.decode("Utf8")
                print("Le serveur a reçu des données:", d)
                if d == "" :
                    return
                datas = d.split(",")
                print("datas",datas)
                distances = dijkstra(graph, datas[1], datas[2])
                #distances = deriv(distances)
                print("distances", distances)
                my_string = tab2req(data,distances)
                con.send(my_string.encode("Utf8"))
            except :
                con.send("Michem404".encode("Utf8"))
                return


# Programme du serveur TCP
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
s.bind(('172.20.10.3', 9998))
mythreads = []


while True:
    s.listen(5)
    print("Serveur: en attente de connexions des clients TCP ...")
    (con, (ip, port)) = s.accept()
    mythread = myThread(ip, port)
    mythread.start()
    mythreads.append(mythread)
for t in mythreads:
    t.join()

=======
import socket
import numpy as np
import json

def Getdata(path):
    with open(path) as f:
        salles = {}
        noeuds = {}
        chemins = []
        # relations = []
        while True:
            line = f.readline()
            if (not line) or (line.strip() == '------------------'):
                break
            line = line.strip()
            data = line.split(",")
            salles[data[0]] = [data[1], data[2]]
        while True:
            line = f.readline()
            if (not line) or (line.strip() == '------------------'):
                break
            line = line.strip()
            data = line.split(",")
            noeuds[data[0]] = [data[1], data[2]]
        while True:
            line = f.readline()

            if (not line) or (line.strip() == '------------------'):
                break
            line = line.strip()
            data = line.split(",")

            chemins.append([data[0], data[1], data[2]])
    return {"salles": salles, "noeuds": noeuds, "chemins": chemins}








import heapq


def dijkstra(graph, start, end):
    # Initialisation
    print("start,end",start,end)
    distances = {node: float('inf') for node in graph}
    distances[start] = 0
    queue = [(0, start, [])]

    while queue:
        # On récupère le noeud avec la plus petite distance
        (cost, node, path) = heapq.heappop(queue)

        # Si on atteint le noeud d'arrivée, on renvoie le chemin
        if node == end:
            return path + [node]

        # Sinon, on continue à explorer les voisins
        for neighbor, weight in graph[node].items():
            new_cost = cost + weight
            if new_cost < distances[neighbor]:
                distances[neighbor] = new_cost
                new_path = path + [node]
                heapq.heappush(queue, (new_cost, neighbor, new_path))

    # Si on ne peut pas atteindre le noeud d'arrivée, on renvoie None
    return None

def deriv(tab):
    for i in range(len(tab)) :
        try :
            if(len(tab[i]) == 1) :
                rem = []
                for j in tab[i+1:]:
                    print(j)
                    if (len(j) == 1) :
                        for z in rem:
                            tab.remove(z)
                            break
                    rem.append(j)
                print(rem)
        except :
            break
    return tab
def tab2req(data,tab):
    salles = data["salles"]
    noeuds = data["noeuds"]
    salles.update(noeuds)
    final = ""
    for i in tab :
        final += str(i)+','+salles[i][0]+','+salles[i][1]+'|'
    return final

# Graphe de test
data = Getdata("PLAN.txt")
edges = data["chemins"]

# Conversion du graphe en dictionnaire
graph = {}
for edge in edges:
    node1 = edge[0]
    node2 = edge[1]
    weight = int(float(edge[2]))

    if node1 not in graph:
        graph[node1] = {}
    if node2 not in graph:
        graph[node2] = {}

    graph[node1][node2] = weight
    graph[node2][node1] = weight


print(graph)
# Calcul des distances les plus courtes
"""
distances = dijkstra(graph, s,end)
# Affichage des distances
print(distances)
print(deriv(distances))
"""
import socket
from threading import Thread
from socketserver import ThreadingMixIn


class myThread(Thread):
    def __init__(self, ip, port):
        Thread.__init__(self)
        self.ip = ip
        self.port = port
        print("[+] Nouveau thread démarré pour " + ip + ":" + str(port))

    def run(self):
        while True:

            try :
                d = con.recv(2048)
                d = d.decode("Utf8")
                print("Le serveur a reçu des données:", d)
                if d == "" :
                    return
                datas = d.split(",")
                print("datas",datas)
                distances = dijkstra(graph, datas[1], datas[2])
                #distances = deriv(distances)
                print("distances", distances)
                my_string = tab2req(data,distances)
                con.send(my_string.encode("Utf8"))
            except :
                con.send("Michem404".encode("Utf8"))
                return


# Programme du serveur TCP
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
s.bind(('172.20.10.3', 9998))
mythreads = []


while True:
    s.listen(5)
    print("Serveur: en attente de connexions des clients TCP ...")
    (con, (ip, port)) = s.accept()
    mythread = myThread(ip, port)
    mythread.start()
    mythreads.append(mythread)
for t in mythreads:
    t.join()

>>>>>>> f0b0c0c92664d0e145796e4e9bc9a92c8b829621
