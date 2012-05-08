{* DO NOT EDIT THIS FILE! Use an override template instead. *}
<div class="block">
    <div class="element">
    <label>{'Use seconds'|i18n( 'design/standard/class/datatype' )}:</label>
    <p>{if $class_attribute.data_int2|eq(1)}
       {'Yes'|i18n( 'design/standard/class/datatype' )}
       {else}
       {'No'|i18n( 'design/standard/class/datatype' )}
       {/if}
    </p>
    </div>

    <div class="element">
    <label>{'Default value'|i18n( 'design/standard/class/datatype' )}:</label>
    <p>
    {switch match=$class_attribute.data_int1}
    {case match=1}
        {'Current datetime'|i18n( 'design/standard/class/datatype' )}
    {/case}

    {case match=2}
        {'Current datetime'|i18n( 'design/standard/class/datatype' )}

        {if $class_attribute.content.year}
        {if $class_attribute.content.year|gt(0)}+{/if}{$class_attribute.content.year} {'year(s)'|i18n( 'design/standard/class/datatype' )}
        {/if}

        {if $class_attribute.content.month}
        {if $class_attribute.content.month|gt(0)}+{/if}{$class_attribute.content.month} {'month(s)'|i18n( 'design/standard/class/datatype' )}
        {/if}

        {if $class_attribute.content.day}
        {if $class_attribute.content.day|gt(0)}+{/if}{$class_attribute.content.day} {'day(s)'|i18n( 'design/standard/class/datatype' )}
        {/if}

        {if $class_attribute.content.hour}
        {if $class_attribute.content.hour|gt(0)}+{/if}{$class_attribute.content.hour} {'hour(s)'|i18n( 'design/standard/class/datatype' )}
        {/if}

        {if $class_attribute.content.minute}
        {if $class_attribute.content.minute|gt(0)}+{/if}{$class_attribute.content.minute} {'minute(s)'|i18n( 'design/standard/class/datatype' )}
        {/if}

        {if and( $class_attribute.data_int2|eq(1), $class_attribute.content.second )}
        {if $class_attribute.content.second|gt(0)}+{/if}{$class_attribute.content.second} {'seconds(s)'|i18n( 'design/standard/class/datatype' )}
        {/if}
    {/case}

    {case}
        <i>{'Empty'|i18n( 'design/standard/class/datatype' )}</i>
    {/case}
    {/switch}
    </p>
    </div>

    <div class="break"></div>
</div>
