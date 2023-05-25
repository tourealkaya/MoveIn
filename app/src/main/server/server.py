# import socket
# import threading
#
# # Adresse IP et port pour le serveur
# IP = "192.168.39.203"
# PORT = 8080
#
# # Création d'une socket TCP pour le serveur
# server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
#
# # Lier la socket à l'adresse IP et au port spécifiés
# server_socket.bind((IP, PORT))
#
# # Démarrer la socket en mode écoute pour accepter les connexions entrantes
# server_socket.listen()
#
# # Dictionnaire pour stocker les connexions clients
# clients = {}
#
# def handle_client(client_socket, client_address):
#     """
#     Fonction pour gérer les communications avec un client connecté.
#     """
#     print(f"Connexion établie avec {client_address}")
#
#     # Ajouter le client à la liste des clients
#     clients[client_address] = client_socket
#
#     while True:
#         # Recevoir les données envoyées par le client
#         data = client_socket.recv(1024).decode()
#
#         # Vérifier si le client a fermé la connexion
#         if not data:
#             # Supprimer le client de la liste des clients
#             del clients[client_address]
#             print(f"Connexion fermée avec {client_address}")
#             break
#
#         # Afficher les données reçues
#         print(f"Reçu de {client_address}: {data}")
#
#         # Envoyer une réponse au client
#         response = f"Reçu '{data}'"
#         client_socket.send(response.encode())
#
# while True:
#     # Accepter une nouvelle connexion client
#     client_socket, client_address = server_socket.accept()
#
#     # Démarrer un nouveau thread pour gérer les communications avec le client
#     client_thread = threading.Thread(target=handle_client, args=(client_socket, client_address))
#     client_thread.start()
#
#
#
#
#
