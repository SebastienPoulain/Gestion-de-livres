document.title = location.pathname
  .substring(location.pathname.lastIndexOf("/") + 1)
  .replace(".html", "")
  .replace(".php", "");

$(document).ready(function() {

  $(function() {
    var mylang = "fr";
    document.getElementsByTagName("html")[0].setAttribute("lang", mylang);
  });


  $("input[type=text]").blur(function() {
    $(this).val(
      $(this)
      .val()
      .trim()
    );
  });
  $("textarea").blur(function() {
    $(this).val(
      $(this)
      .val()
      .trim()
    );
  });

});