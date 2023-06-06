package project.movein.backend

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

data class Person(val name: String, val address: String)
class michem {
    private val peopleList = ArrayList<Array<String>>()



    fun getData(inputStream: InputStream): Map<String, Any> {
        val salles = mutableMapOf<String, List<String>>()
        val noeuds = mutableMapOf<String, List<String>>()
        val chemins = mutableListOf<List<String>>()

        val reader = BufferedReader(InputStreamReader(inputStream))
        while (true) {
            val line = reader.readLine()?.trim() ?: break
            if (line == "------------------") {
                break
            }
        }
        while (true) {
            val line = reader.readLine()?.trim() ?: break
            if (line == "------------------") {
                break
            }
            val data = line.split(",")
            salles[data[0]] = listOf(data[1], data[2])
        }

        while (true) {
            val line = reader.readLine()?.trim() ?: break
            if (line == "------------------") {
                break
            }
            val data = line.split(",")
            noeuds[data[0]] = listOf(data[1], data[2])
        }

        while (true) {
            val line = reader.readLine()?.trim() ?: break
            if (line == "------------------") {
                break
            }
            val data = line.split(",")
            chemins.add(listOf(data[0], data[1], data[2]))
        }

        reader.close()

        return mapOf("salles" to salles, "noeuds" to noeuds, "chemins" to chemins)
    }



    fun dijkstra(graph: Map<String, Map<String, Int>>, s: String, end: String): List<String>? {
        // Ajouter des personnes à la liste
        peopleList.add(arrayOf("Laurent Heutte", "U2.1.14"))
        peopleList.add(arrayOf("Youssouf SAIDALI", "U2.1.38"))
        peopleList.add(arrayOf("Mathieu BLOSSIER", "U2.1.39"))
        var start = s
        // Afficher les personnes de la liste
        for (personData in peopleList) {
            val name = personData[0]
            val address = personData[1]
            if(start.equals(name)){
                start = address
            }
        }


        val distances = HashMap<String, Int>()
        val visited = HashSet<String>()
        val previous = HashMap<String, String>()
        graph.keys.forEach { node -> distances[node] = Int.MAX_VALUE }
        distances[start] = 0

        while (true) {
            val currentNode = getClosestUnvisitedNode(distances, visited)
            if (currentNode == null) {
                break
            }
            visited.add(currentNode)

            if (currentNode == end) {
                return buildPath(previous, end)
            }

            for ((neighbor, weight) in graph[currentNode]!!) {
                if (neighbor !in visited) {
                    val newDistance = distances[currentNode]!! + weight
                    if (newDistance < distances[neighbor]!!) {
                        distances[neighbor] = newDistance
                        previous[neighbor] = currentNode
                    }
                }
            }
        }

        return null
    }

    private fun getClosestUnvisitedNode(distances: Map<String, Int>, visited: Set<String>): String? {
        var closestNode: String? = null
        var closestDistance = Int.MAX_VALUE
        for ((node, distance) in distances) {
            if (node !in visited && distance < closestDistance) {
                closestNode = node
                closestDistance = distance
            }
        }
        return closestNode
    }

    private fun buildPath(previous: Map<String, String>, end: String): List<String> {
        val path = mutableListOf<String>()
        var current = end
        while (current != "") {
            path.add(current)
            current = previous[current] ?: ""
        }
        path.reverse()
        return path
    }

    fun deriv(tab: MutableList<String>): List<String> {
        var count = 0
        val rem = mutableListOf<String>()
        var i = 0
        while (i < tab.size) {
            count++

            if (tab[i].length <= 2 || i == tab.size - 1) {
                var z = 1
                while (z < count - 1) {
                    rem.add(tab[i - z])
                    println(tab[i - z])
                    z++
                }
                count = 1
            }
            i++
        }
        tab.removeAll(rem)
        return tab
    }


    fun getGraph(edges: List<List<String>>): Map<String, Map<String, Int>> {
        val graph = HashMap<String, HashMap<String, Int>>()
        for (edge in edges) {
            val node1 = edge[0]
            val node2 = edge[1]
            val weight = edge[2].toInt()

            if (!graph.containsKey(node1)) {
                graph[node1] = HashMap()
            }
            if (!graph.containsKey(node2)) {
                graph[node2] = HashMap()
            }

            graph[node1]?.put(node2, weight)
            graph[node2]?.put(node1, weight)
        }

        return graph
    }

    fun tab2req(data: Map<String, Any>, tab: List<String>): String {
        val salles = data["salles"] as Map<String, List<String>>
        val noeuds = data["noeuds"] as Map<String, List<String>>
        val mergedMap = salles.toMutableMap()
        mergedMap.putAll(noeuds)
        var finalString = ""
        for (i in tab) {
            finalString += "$i,${mergedMap[i]?.get(0)?.toInt()},${mergedMap[i]?.get(1)?.toInt()}|"
        }
        return finalString
    }
}