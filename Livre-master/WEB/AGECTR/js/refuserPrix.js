$(document).ready(function() {

  $(document).on("click", ".deleteConcession", function() {


    var id = $(this).closest('tr').attr('id');

    $("#idSupprimer").val(id);


    $("#supprimerModal").modal("show");

  });

  $(document).on("click", "#supprimer", function() {

    id = $("#idSupprimer").val();

    $.ajax({
      url: "php/refuserPrix.php",
      method: "POST",
      data: {
        id: id
      },
      success: function(data) {
        if (data == "success") {
          alert("Le changement de prix a été refusé avec succès");
          $("#supprimerModal").modal("hide");
          $('#tblModifConcession').DataTable().ajax.reload();
        } else {
          alert(data);
        }
      }
    });

  });

});