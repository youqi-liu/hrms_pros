<template>
<div id="main" class="app-main">
    <router-view></router-view>
</div>
</template>

<script>
export default {
    data() {
        return {};
    },
    mounted() {},
    beforeDestroy() {},
    methods: {}
};
</script>

<style lang="less">
html,
body {
    width: 100%;
    height: 100%;
    background: linear-gradient(135deg, #eceff1 0%, #cfd8dc 100%);
}

.app-main {
    width: 100%;
    height: 100%;
}

.br button {
    margin-right: 8px;
    transition: all 0.3s ease;
}

.br button:hover {
    transform: translateY(-1px);
    box-shadow: 0 2px 8px rgba(255, 111, 0, 0.3);
}

.operation button {
    margin-right: 8px;
    transition: all 0.3s ease;
}

.operation button:hover {
    transform: translateY(-1px);
}

.operation .brr button {
    margin-right: 0px !important;
}

.ivu-btn-text:focus {
    box-shadow: none !important;
}

.ivu-tag {
    cursor: pointer;
    transition: all 0.3s ease;
}

.ivu-tag:hover {
    transform: scale(1.05);
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

.block-tool .ivu-tooltip,
.block-tool .ivu-tooltip-rel {
    display: block;
}

.block-pop .ivu-poptip,
.block-pop .ivu-poptip-rel {
    display: block;
}

.form-wangEditor,
.form-quill {
    .ivu-form-item-content {
        line-height: unset !important;
    }
}

/* 全局卡片样式优化 */
.ivu-card {
    border-radius: 12px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
    transition: all 0.3s ease;
    border: none;
    background: #fff;
}

.ivu-card:hover {
    box-shadow: 0 6px 20px rgba(0, 0, 0, 0.12);
    transform: translateY(-2px);
}

.ivu-card-head {
    border-bottom: 2px solid #f0f0f0;
    font-weight: 600;
    color: #333;
}

/* 表格样式优化 */
.ivu-table {
    border-radius: 12px;
    overflow: hidden;
}

.ivu-table-wrapper {
    border-radius: 12px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.ivu-table th {
    background: linear-gradient(135deg, #ff6f00 0%, #ff9800 100%);
    color: #fff;
    font-weight: 600;
}

.ivu-table-tbody tr:hover {
    background: rgba(255, 111, 0, 0.05);
}

/* 按钮样式优化 */
.ivu-btn-primary {
    background: linear-gradient(135deg, #ff6f00 0%, #ff9800 100%);
    border: none;
    transition: all 0.3s ease;
}

.ivu-btn-primary:hover {
    background: linear-gradient(135deg, #ff9800 0%, #ff6f00 100%);
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(255, 111, 0, 0.4);
}

/* 输入框样式优化 */
.ivu-input:focus {
    border-color: #ff6f00;
    box-shadow: 0 0 0 2px rgba(255, 111, 0, 0.2);
}

.ivu-select-selection:focus {
    border-color: #ff6f00;
    box-shadow: 0 0 0 2px rgba(255, 111, 0, 0.2);
}

.ivu-input,
.ivu-select-selection {
    border-radius: 6px;
    transition: all 0.3s ease;
}

/* 模态框样式优化 */
.ivu-modal {
    border-radius: 12px;
    overflow: hidden;
}

.ivu-modal-header {
    background: linear-gradient(135deg, #1a237e 0%, #0d47a1 100%);
    color: #fff;
    border-bottom: none;
}

.ivu-modal-header-inner {
    color: #fff;
}

.ivu-modal-close .ivu-icon {
    color: #fff;
}

/* 分页样式优化 */
.ivu-page-item-active {
    background: linear-gradient(135deg, #ff6f00 0%, #ff9800 100%);
    border-color: transparent;
}

.ivu-page-item {
    border-radius: 6px;
    transition: all 0.3s ease;
}

.ivu-page-item:hover {
    border-color: #ff6f00;
    color: #ff6f00;
}

/* 面包屑样式优化 */
.ivu-breadcrumb {
    font-size: 14px;
}

.ivu-breadcrumb-item-link {
    color: #515a6e;
    transition: all 0.3s ease;
}

.ivu-breadcrumb-item-link:hover {
    color: #ff6f00;
}

/* 消息提示优化 */
.ivu-message-notice-content {
    border-radius: 8px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

/* 下拉菜单优化 */
.ivu-dropdown {
    border-radius: 8px;
}

.ivu-dropdown-menu {
    border-radius: 8px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.ivu-dropdown-item:hover {
    background: rgba(255, 111, 0, 0.1);
    color: #ff6f00;
}
</style>
