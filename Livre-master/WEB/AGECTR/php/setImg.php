<?php

include "BD.php";

    if (isset($_FILES["image"])) {
        $id = $_POST["idImg"];
        $filename = $_FILES["image"]["name"];

        $sql = "UPDATE book set imgPath = :img  where id = :id";
        $stmt = $conn->prepare($sql);
        $stmt->execute(array(':img' => $filename,':id' => $id));

        if (file_exists("../img/" . $filename)) {
            echo $filename . " existe déjà, veuillez changer le nom de votre fichier";
        } else {
            move_uploaded_file($_FILES["image"]["tmp_name"], "../img/" . $filename);
            echo "success";
        }
    } else {
        echo "error";
    }
$conn = null;
