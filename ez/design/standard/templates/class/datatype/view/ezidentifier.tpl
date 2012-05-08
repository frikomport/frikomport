{* DO NOT EDIT THIS FILE! Use an override template instead. *}
<div class="block">
    <div class="element">
        <label>{'Pretext'|i18n( 'design/standard/class/datatype' )}:</label>
        <p>
            {if $class_attribute.data_text1}
                {$class_attribute.data_text1|wash}
            {else}
                <i>{'Empty'|i18n( 'design/standard/class/datatype' )}</i>
            {/if}
        </p>
    </div>

    <div class="element">
        <label>{'Posttext'|i18n( 'design/standard/class/datatype' )}:</label>
        <p>{if $class_attribute.data_text2}
               {$class_attribute.data_text2|wash}
           {else}
               <i>{'Empty'|i18n( 'design/standard/class/datatype' )}</i>
           {/if}
        </p>
    </div>


    <div class="element">
        <label>{'Digits'|i18n( 'design/standard/class/datatype' )}:</label>
        <p>{$class_attribute.data_int2}</p>
    </div>

    <div class="element">
        <label>{'Start value'|i18n( 'design/standard/class/datatype' )}:</label>
        <p>{$class_attribute.data_int1}</p>
    </div>

    <div class="break"></div>
</div>

<div class="block">
    <label>{'Current value'|i18n( 'design/standard/class/datatype' )}:</label>
    {if gt( $class_attribute.data_int2|sub( $class_attribute.data_int3|count ), 0 )}
        <p>{$class_attribute.data_text1|wash}{$class_attribute.data_int3|indent( $class_attribute.data_int2|sub( $class_attribute.data_int3|count ), 'custom', '0' )}{$class_attribute.data_text2|wash}</p>
    {else}
        <p>{$class_attribute.data_text1|wash}{$class_attribute.data_int3}{$class_attribute.data_text2|wash}</p>
    {/if}
</div>
