<template>
  <div class="tutor-list-page">
    <!-- 搜索栏 -->
    <div class="search-bar">
      <el-input v-model="searchSubject" placeholder="搜索科目，如：数学" clearable @keyup.enter="doSearch">
        <template #append>
          <el-button @click="doSearch">搜索</el-button>
        </template>
      </el-input>
    </div>

    <!-- 筛选条件 -->
    <div class="filter-bar">
      <el-select v-model="searchDistrict" placeholder="区域" clearable style="width:120px" @change="doSearch">
        <el-option label="不限" value="" />
        <el-option label="海淀区" value="海淀区" />
        <el-option label="朝阳区" value="朝阳区" />
        <el-option label="西城区" value="西城区" />
        <el-option label="东城区" value="东城区" />
      </el-select>
    </div>

    <!-- 教员列表 -->
    <div class="tutor-list">
      <div v-if="tutors.length === 0" class="empty-state">
        <p class="empty-icon">🔍</p>
        <p class="empty-text">暂无匹配的教员</p>
      </div>

      <div
        v-for="tutor in tutors"
        :key="tutor.id"
        class="card tutor-card"
        @click="goDetail(tutor.id)"
      >
        <div class="tutor-header">
          <div class="avatar">{{ tutor.nickname?.[0] || '教' }}</div>
          <div class="tutor-info">
            <p class="name">{{ tutor.nickname || '大学生教员' }}</p>
            <p class="school">{{ tutor.university }} · {{ tutor.major }}</p>
          </div>
          <div class="price">
            <span class="amount">{{ tutor.hourlyRate }}</span>
            <span class="unit">元/小时</span>
          </div>
        </div>

        <div class="tutor-tags">
          <el-tag v-for="sub in parseSubjects(tutor.subjects)" :key="sub" size="small" round type="primary" style="margin-right:6px;margin-bottom:4px">
            {{ sub }}
          </el-tag>
        </div>

        <p v-if="tutor.bio" class="tutor-bio">{{ tutor.bio }}</p>

        <div class="tutor-footer">
          <span class="rating">⭐ {{ tutor.rating?.toFixed(1) || '暂无' }}</span>
          <span class="orders">已完成 {{ tutor.orderCount || 0 }} 单</span>
          <span v-if="tutor.districts" class="districts">📍 {{ tutor.districts }}</span>
        </div>
      </div>
    </div>

    <!-- 加载更多 -->
    <div v-if="hasMore" class="load-more">
      <el-button text @click="loadMore">加载更多</el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { searchTutors } from '@/api/tutor'

const router = useRouter()

const tutors = ref([])
const searchSubject = ref('')
const searchDistrict = ref('')
const page = ref(1)
const hasMore = ref(true)

onMounted(() => {
  doSearch()
})

async function doSearch() {
  page.value = 1
  tutors.value = []
  hasMore.value = true
  await loadTutors()
}

async function loadTutors() {
  try {
    const res = await searchTutors({
      subject: searchSubject.value || undefined,
      district: searchDistrict.value || undefined,
      page: page.value,
      size: 10,
    })
    const list = res.data || []
    if (page.value === 1) {
      tutors.value = list
    } else {
      tutors.value.push(...list)
    }
    hasMore.value = list.length >= 10
  } catch {
    // 已处理
  }
}

function loadMore() {
  page.value++
  loadTutors()
}

function parseSubjects(subjects) {
  if (!subjects) return []
  return subjects.split(',').slice(0, 4)
}

function goDetail(id) {
  router.push(`/tutor/${id}`)
}
</script>

<style lang="scss" scoped>
.tutor-list-page {
  min-height: 100vh;
  background: var(--color-bg);
  padding: 16px;
}

.search-bar {
  margin-bottom: 12px;
}

.filter-bar {
  margin-bottom: 16px;
}

.tutor-list {
  .empty-state {
    text-align: center;
    padding: 60px 0;

    .empty-icon { font-size: 48px; margin-bottom: 12px; }
    .empty-text { color: var(--color-text-placeholder); }
  }
}

.tutor-card {
  cursor: pointer;

  &:active { opacity: 0.9; }

  .tutor-header {
    display: flex;
    align-items: center;
    gap: 12px;
    margin-bottom: 10px;

    .avatar {
      width: 44px;
      height: 44px;
      border-radius: 50%;
      background: var(--color-primary);
      color: white;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 16px;
      font-weight: 600;
      flex-shrink: 0;
    }

    .tutor-info {
      flex: 1;

      .name {
        font-size: var(--font-size-md);
        font-weight: 600;
      }

      .school {
        font-size: var(--font-size-xs);
        color: var(--color-text-secondary);
        margin-top: 2px;
      }
    }

    .price {
      text-align: right;
      flex-shrink: 0;

      .amount {
        font-size: var(--font-size-xl);
        font-weight: 700;
        color: var(--color-primary);
      }

      .unit {
        font-size: var(--font-size-xs);
        color: var(--color-text-placeholder);
      }
    }
  }

  .tutor-tags {
    margin-bottom: 8px;
  }

  .tutor-bio {
    font-size: var(--font-size-sm);
    color: var(--color-text-secondary);
    line-height: 1.5;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
    margin-bottom: 8px;
  }

  .tutor-footer {
    display: flex;
    gap: 16px;
    font-size: var(--font-size-xs);
    color: var(--color-text-placeholder);
  }
}

.load-more {
  text-align: center;
  padding: 16px 0;
}
</style>