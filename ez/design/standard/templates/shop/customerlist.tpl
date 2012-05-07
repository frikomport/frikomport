{* DO NOT EDIT THIS FILE! Use an override template instead. *}
<form action={concat("/shop/customerlist")|ezurl} method="post" name="Customerlist">
<div class="maincontentheader">
  <h1>{"Customer list"|i18n("design/standard/shop")}</h1>
</div>

{section show=$customer_list}
<table class="list" width="100%" cellspacing="0" cellpadding="0" border="0">
<tr>
        <th>
        {"Customer"|i18n("design/standard/shop")}
        </th>
        <th>
        {"Number of orders"|i18n("design/standard/shop")}
        </th>
        <th>
        {"Total ex. VAT"|i18n("design/standard/shop")}
        </th>
        <th>
        {"Total inc. VAT"|i18n("design/standard/shop")}
        </th>
</tr>

{def $currency = false()
     $locale = false()
     $symbol = false()
     $order_count_text = ''
     $sum_ex_vat_text = ''
     $sum_inc_vat_text = ''
     $br_tag = ''}

{section var="Customer" loop=$customer_list sequence=array(bglight,bgdark)}

    {set order_count_text = ''
         sum_ex_vat_text = ''
         sum_inc_vat_text = ''
         br_tag = ''}

    {foreach $Customer.orders_info as $currency_code => $order_info }

        {if $currency_code}
            {set currency = fetch( 'shop', 'currency', hash( 'code', $currency_code ) ) }
        {else}
            {set currency = false()}
        {/if}
        {if $currency}
            {set locale = $currency.locale
                 symbol = $currency.symbol}
        {else}
            {set locale = false()
                 symbol = false()}
        {/if}

        {set order_count_text = concat( $order_count_text, $br_tag, $order_info.order_count) }
        {set sum_ex_vat_text = concat($sum_ex_vat_text, $br_tag, $order_info.sum_ex_vat|l10n( 'currency', $locale, $symbol )) }
        {set sum_inc_vat_text = concat($sum_inc_vat_text, $br_tag, $order_info.sum_inc_vat|l10n( 'currency', $locale, $symbol )) }

        {if $br_tag|not()}
            {set br_tag = '<br />'}
        {/if}
    {/foreach}

    <tr>
        <td class="{$Customer.sequence}"><a href={concat("/shop/customerorderview/",$Customer.user_id,"/", $Customer.email)|ezurl}>{$Customer.account_name}</a></td>
        <td class="{$Customer.sequence}">{$order_count_text}</td>
        <td class="{$Customer.sequence}">{$sum_ex_vat_text}</td>
        <td class="{$Customer.sequence}">{$sum_inc_vat_text}</td>
    </tr>
{/section}
{undef}
</table>
{section-else}

<div class="feedback">
  <h2>{"The customer list is empty"|i18n("design/standard/shop")}</h2>
</div>

{/section}

{include name=navigator
         uri='design:navigator/google.tpl'
         page_uri='/shop/customerlist'
         item_count=$customer_list_count
         view_parameters=$view_parameters
         item_limit=$limit}
</form>
