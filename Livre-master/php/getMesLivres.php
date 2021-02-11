<?php 

if (isset($_POST['id'])){
    $sql = "SELECT title, author, publisher, edition, barcode FROM book b 
    inner join concession c on c.idBook = b.id
    WHERE idCustomer = :idCustomer;";
    $stmt = $conn->prepare($sql);
    $stmt -> execute(array(':idCustomer' => $_POST['id']));
    $livres = $stmt->fetch();

    if ($livres == true){
       echo json_encode($livres);
    }
    else{
        $livreVide = array(
    "title"=> null,
    "author"=> null,
    "publisher"=> null,
    "edition"=> null,
    "barcode"=> null);
	echo json_encode($livreVide);
    }


}
else echo json_encode("error");

$conn = null;