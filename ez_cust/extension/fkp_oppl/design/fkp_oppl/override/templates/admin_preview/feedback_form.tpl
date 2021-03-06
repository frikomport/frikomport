{* Feedback form - Admin preview *}
<div class="content-view-full">
    <div class="class-feedback-form">

        <h1>{$node.name|wash()}</h1>

        {* Description. *}
        <div class="attribute-short">
            {attribute_view_gui attribute=$node.object.data_map.description}
        </div>

        {* Email address (information collector). *}
        <div class="attribute-email">
            <label>{'Your E-mail address'|i18n( 'design/admin/preview/feedbackform' )}:</label>
            {attribute_view_gui attribute=$node.object.data_map.email}
        </div>

        {* Subject (information collector). *}
        <div class="attribute-subject">
            <label>{'Subject'|i18n( 'design/admin/preview/feedbackform' )}:</label>
            {attribute_view_gui attribute=$node.object.data_map.subject}
        </div>

        {* Message (information collector). *}
        <div class="attribute-message">
            <label>{'Message'|i18n( 'design/admin/preview/feedbackform' )}:</label>
            {attribute_view_gui attribute=$node.object.data_map.message}
        </div>

        {* Recipient. *}
        <div class="content-control">
            <label>{'Recipient'|i18n( 'design/admin/preview/feedbackform' )}:</label>
            {attribute_view_gui attribute=$node.object.data_map.recipient}
        </div>

    </div>
</div>
