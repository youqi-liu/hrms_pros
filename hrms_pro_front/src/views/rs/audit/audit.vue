<template>
<div class="search">
    <Card>
        <Row>
            <Table :loading="loading" border :columns="columns" :data="userData" ref="table" sortable="custom" @on-sort-change="changeSort" @on-selection-change="changeSelect"></Table>
        </Row>
    </Card>
</div>
</template>

<script>
import {
    getWageBankCardVoList,
    setRosterWageBankNumber,
    auditWage
} from "./api.js";
export default {
    name: "yongYingIndex",
    components: {},
    data() {
        return {
            loading: false,
            columns: [{
                    type: "index",
                    width: 60,
                    align: "center",
                },
                {
                    title: "申请人",
                    key: "userName",
                    minWidth: 80,
                    sortable: true,
                },
                {
                    title: "申请金额",
                    key: "moneyData",
                    minWidth: 80,
                    sortable: true,
                },
                {
                    title: "申请时间",
                    key: "salaryTime",
                    minWidth: 80,
                    sortable: true,
                },
                {
                    title: "月份",
                    key: "mouth",
                    minWidth: 80,
                    sortable: true,
                },
                {
                    title: "状态",
                    key: "status",
                    minWidth: 80,
                    sortable: true,
                    render: (h, params) => {
                        if (params.row.status == 1) {
                            return h("div", [
                                h(
                                    "span", {
                                        style: {
                                            color: "#3CB371",
                                        },
                                    },
                                    "审核通过"
                                ),
                            ]);
                        } else {
                            return h("div", [
                                h(
                                    "span", {
                                        style: {
                                            color: "#ff0000",
                                        },
                                    },
                                    "待审核"
                                ),
                            ]);
                        }
                    },
                },
                {
                    title: "操作",
                    key: "action",
                    align: "center",
                    width: 200,
                    render: (h, params) => {
                        if (params.row.status == 0) {
                            return h("div", [
                                h(
                                    "Button", {
                                        props: {
                                            type: "error",
                                            size: "small",
                                        },
                                        on: {
                                            click: () => {
                                                this.remove(params.row);
                                            }
                                        }
                                    },
                                    "审核"
                                )
                            ]);
                        } else {
                            return h("div", [
                                h(
                                    "span", {
                                        style: {
                                            color: "#ff0000",
                                        },
                                    },
                                    "已审核"
                                ),
                            ]);
                        }

                    }
                },
                {
                    title: "审核人",
                    key: "auditName",
                    minWidth: 80,
                    sortable: true,
                },
                {
                    title: "审核时间",
                    key: "auditTime",
                    minWidth: 80,
                    sortable: true,
                },
            ],
            userData: [],
            searchForm: {
                pageNumber: 1,
                pageSize: 15,
                status: '0',
                sort: "createTime",
                order: "asc",
                username: '',
                jobnumber: '',
                systemGuishu: '1314520'
            },
            total: 0,
            nowInputRow: {}
        };
    },
    methods: {
        init() {
            this.getDataList();
        },
        getDataList() {
            var that = this;
            that.loading = true;
            getWageBankCardVoList(this.searchForm).then(res => {
                console.log(res);
                that.userData = res.result;
                that.loading = false;
            })
        },
        saveByInputRemark() {
            var that = this;
            setRosterWageBankNumber({
                id: that.nowInputRow.id,
                moneyData: that.nowInputRow.moneyData
            }).then(res => {
                if (res.success) {
                    this.$Message.success("修改成功");
                } else {
                    this.$Message.error("修改失败");
                }
            })
        },
        changePage(v) {
            this.searchForm.pageNumber = v;
            this.getDataList();
        },
        changePageSize(v) {
            this.searchForm.pageSize = v;
            this.getDataList();
        },
        remove(v) {
            var that = this;
            this.$Modal.confirm({
                title: "再次确认",
                content: "您确认审核工资提现申请?",
                loading: true,
                onOk: () => {
                    auditWage({
                        id: v.id
                    }).then(res => {
                        this.$Modal.remove();
                        if (res.success) {
                            this.$Message.success("操作成功");
                            that.getDataList();
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
