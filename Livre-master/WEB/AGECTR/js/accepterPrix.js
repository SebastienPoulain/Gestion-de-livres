$(document).ready(function() {

  $(document).on("click", ".acceptConcession", function() {

    var prix = $(event.target)
      .closest("tr")
      .find($("td:nth-child(9)"))
      .text();

    var ancien = $(event.target)
      .closest("tr")
      .find($("td:nth-child(8)"))
      .text();

    ancien = ancien.replace("$", "");
    ancien = ancien.trim();

    prix = prix.replace("$", "");
    prix = prix.trim();

    var id = $(this).closest('tr').attr('id');

    $("#idAccepter").val(id);
    $("#prix").val(prix);
    $("#ancienPrix").val(ancien);

    $("#accepterModal").modal("show");

  });

  $(document).on("click", "#accepter", function() {

    id = $("#idAccepter").val();
    var prix = $("#prix").val();
    var ancien = $("#ancienPrix").val();
    $.ajax({
      url: "php/accepterPrix.php",
      method: "POST",
      data: {
        id: id,
        prix: prix,
        ancien: ancien
      },
      success: function(data) {
        if (data == "success") {
          alert("Le prix de la concession a été validé avec succès");
          $("#accepterModal").modal("hide");
          $('#tblModifConcession').DataTable().ajax.reload();
        } else {
          alert(data);
        }
      }
    });

  });

});