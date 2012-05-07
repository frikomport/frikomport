<?php

function changeAttributeValue($origTxt, $tag, $attr, $prefixOld, $prefixNew) {
//    print("origTxt=[".htmlentities($origTxt)."]<p>");

    $pattern = '/<('.$tag.'(.|\n)*?'.$attr.')="'.$prefixOld.'([^"]*)/i';
    $replacePtn = '<$1="'.$prefixNew.'$3';
    $resTxt = preg_replace($pattern, $replacePtn, $origTxt);

    $jsessionidPos = strpos($resTxt, ';jsessionid=');
    if ($jsessionidPos !== false) {
        $resTxt = substr($resTxt, 0, $jsessionidPos).substr($resTxt, $jsessionidPos+44);
    }

/*
print("<p>Orig:[".htmlentities($origTxt)."]<br/>");
print("Repl:[".htmlentities($resTxt)."]<p>");
print("<hr><p>Orig:[".($origTxt)."]<br/>");
print("Repl:[".($resTxt)."]<p>");
*/

return $resTxt;
} //end function

?>
