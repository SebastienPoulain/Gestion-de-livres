$(document).ready(function() {

  $(document).on("click", ".deleteLivre", function() {

    var livre = $(event.target)
      .closest("tr")
      .find($("td:nth-child(1)"))
      .text();

    $("#userName2").text(livre + " ?");

    var id = $(this).closest('tr').attr('id');

    $("#idSupprimer").val(id);


    $("#supprimerModal").modal("show");

  });

  $(document).on("click", "#supprimer", function() {

    id = $("#idSupprimer").val();

    $.ajax({
      url: "php/refuserLivre.php",
      method: "POST",
      data: {
        id: id
      },
      success: function(data) {
        if (data == "success") {
          alert("Le livre a été supprimé avec succès");
          $("#supprimerModal").modal("hide");
          $('#tblLivre').DataTable().ajax.reload();
        } else {
          alert(data);
        }
      }
    });

  });

});