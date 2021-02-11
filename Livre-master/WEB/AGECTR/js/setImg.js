$(document).ready(function() {


  $(document).on("click", ".changeImg", function() {


    var id = $(this).closest('tr').attr('id');

    $("#idImg").val(id);


    $("#imgModal").modal("show");

  });

  $(document).on("click", "#fermer", function() {

    $("#image").val("");

  });

  $("#ajoutImg").submit(function(event) {
    event.preventDefault();
    var image_name = $("#image").val();
    if (image_name == "") {
      alert("Vous devez choisir une image");
      return false;
    } else {
      var extension = $("#image")
        .val()
        .split(".")
        .pop()
        .toLowerCase();
      if (jQuery.inArray(extension, ["gif", "png", "jpg", "jpeg"]) == -1) {
        alert("Type de fichier non valide pour une image");
        $("#image").val("");
        return false;
      }
    }

    $.ajax({
      url: "php/setImg.php",
      method: "POST",
      data: new FormData(this),
      contentType: false,
      processData: false,
      success: function(data) {
        if (data == "success") {
          alert("L'image a été mis à jour avec succès");
          $("#image").val("");
          $("#imgModal").modal("hide");
          $('#tblLivre').DataTable().ajax.reload();
        } else {
          alert(data);
        }
      }
    });
  });

});