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
    setRosterWageBankNumber
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
                    title: "工号",
                    key: "username",
                    minWidth: 80,
                    sortable: true,
                },
                {
                    title: "姓名",
                    key: "nickname",
                    minWidth: 80,
                    sortable: true,
                },
                {
                    title: "薪资标准",
                    key: "moneyData",
                    minWidth: 190,
                    sortable: true,
                    render: (h, params) => {
                        var that = this;
                        return h('div', [
                            h('Input', {
                                props: {
                                    type: 'text',
                                    size: 'small',
                                    value: params.row.moneyData
                                },
                                on: {
                                    "on-blur": function () {
                                        that.nowInputRow = params.row;
                                        that.saveByInputRemark();
                                    },
                                    input: (e) => {
                                        params.row.moneyData = e;
                                    }
                                }
                            })
                        ])
                    }
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
