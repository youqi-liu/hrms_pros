<template>
<div>
    <Row class="header">
        <div class="header-logo" style="display: flex; align-items: center; justify-content: center;">
            <Icon type="ios-people" size="50" style="color: #ff6f00; margin-right: 10px;"/>
            <span style="color: #333; font-size: 24px; font-weight: bold;">HRMS Pro</span>
        </div>
        <div class="description">人事管理系统</div>
    </Row>
</div>
</template>

<script>
export default {
    name: "header"
};
</script>

<style lang="less">
.header {
    margin-top: 8vh;
    margin-bottom: 5vh;
    text-align: center;

    .description {
        font-size: 14px;
        color: rgba(0, 0, 0, 0.45);
        margin-top: 1vh;
    }
}
</style>
