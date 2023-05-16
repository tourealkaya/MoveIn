#!/usr/bin/env python
# coding: utf-8

# In[ ]:


import socket

# Adresse IP et port du serveur
IP = "192.168.39.203"
PORT = 8080

# Création d'une socket TCP pour le client
client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

# Connexion au serveur
client_socket.connect((IP, PORT))

while True:
    # Demander à l'utilisateur d'entrer des données à envoyer au serveur
    message = input("Entrez un message: ")
    
    # Envoyer les données au serveur
    client_socket.send(message.encode())
    
    # Recevoir la réponse du serveur
    response = client_socket.recv(1024).decode()
    
    # Afficher la réponse du serveur
    print(f"Réponse du serveur: {response}")

