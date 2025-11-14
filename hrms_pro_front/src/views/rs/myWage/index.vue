<template>
<div class="search">
    <Card>
        <Form ref="form" :label-width="90" label-position="left">
            <FormItem label="薪资标准" prop="street">
                <Input v-model="myMoneyData" disabled style="width: 300px" />
            </FormItem>
            <FormItem label="申请工资" prop="street">
                <Button v-show="findWageFxStatus==2" type="success" @click="sqgztx">申请工资提现</Button>
                <Input v-show="findWageFxStatus==0" value="本月已申请,请等待审核" disabled style="width: 300px" />
                <Input v-show="findWageFxStatus==1" value="本月提现审核通过" disabled style="width: 300px" />
            </FormItem>
        </Form>
    </Card>
</div>
</template>

<script>
import {
    getMyWage,
    applyWage,
    findWage,
} from "./api.js";

export default {
    name: "yongYingIndex",
    components: {},
    data() {
        return {
            myMoneyData: 0,
            findWageFxStatus: 2
        };
    },
    methods: {
        init() {
            this.getMyWageFx();
            this.findWageFx();
        },
        getMyWageFx() {
            var that = this;
            getMyWage().then(res => {
                if (res.success) {
                    that.myMoneyData = res.result.moneyData;
                }
            })
        },
        findWageFx() {
            var that = this;
            findWage().then(res => {
                console.log(res);
                if (res.success) {
                    that.findWageFxStatus = res.result;
                }
            })
        },
        sqgztx() {
            var that = this;
            this.$Modal.confirm({
                title: "再次确认",
                content: "确认提交工资提现申请?",
                loading: true,
                onOk: () => {
                    applyWage().then(res => {
                        this.$Modal.remove();
                        if (res.success) {
                            that.findWageFx();
                            this.$Message.success("操作成功");
                        }
                    });
                }
            });
        },
    },
    mounted() {
        this.init();
    }
};
</script>

<style lang="less">
.search {
    .operation {
        margin-bottom: 2vh;
    }

    .select-count {
        font-weight: 600;
        color: #40a9ff;
    }

    .select-clear {
        margin-left: 10px;
    }

    .page {
        margin-top: 2vh;
    }

    .drop-down {
        margin-left: 5px;
    }
}
</style>
