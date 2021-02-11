$(document).ready(function() {

  $(document).on("click", ".deleteConcession", function() {

    id = $(this).closest('tr').attr('id');

    $("#idSupprimer").val(id);


    $("#supprimerModal").modal("show");

  });

  $(document).on("click", "#supprimer", function() {

    id = $("#idSupprimer").val();

    $.ajax({
      url: "php/refuserConcession.php",
      method: "POST",
      data: {
        id: id
      },
      success: function(data) {
        if (data == "success") {
          alert("La concession a été supprimée avec succès");
          $("#supprimerModal").modal("hide");
          $('#tblValidationConcession').DataTable().ajax.reload();
        } else {
          alert(data);
        }
      }
    });

  });

});