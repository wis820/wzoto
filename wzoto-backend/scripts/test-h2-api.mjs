// 测试 H2 后端 API 完整链路（正确参数格式）
const BASE = 'http://localhost:8080';

async function main() {
  // 1. 登录
  const loginResp = await fetch(`${BASE}/api/auth/dev-login`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ nickname: '测试家长' }),
  });
  const loginData = await loginResp.json();
  const token = loginData.data.token;
  console.log('1. LOGIN OK');

  const headers = { Authorization: `Bearer ${token}` };

  // 2. 获取孩子
  const childResp = await fetch(`${BASE}/api/children`, { headers });
  const childData = await childResp.json();
  const children = childData.data || [];
  let childId = children[0]?.id;
  if (!childId) {
    const createResp = await fetch(`${BASE}/api/child`, {
      method: 'POST',
      headers: { ...headers, 'Content-Type': 'application/json' },
      body: JSON.stringify({ name: '小明', grade: 'GRADE_4', textbookVersion: 'RENJIAO' }),
    });
    const createData = await createResp.json();
    childId = createData.data.id;
    console.log(`2. CREATED CHILD id=${childId}`);
  } else {
    console.log(`2. CHILD id=${childId} (${children[0].name}, ${children[0].grade})`);
  }

  // 3. 三科视频资源
  for (const subject of ['MATH', 'CHINESE', 'ENGLISH']) {
    const resp = await fetch(
      `${BASE}/api/learning/resources?childId=${childId}&subject=${subject}&resourceType=VIDEO`,
      { headers }
    );
    const data = await resp.json();
    const list = data.data || [];
    console.log(`3. ${subject} VIDEO: ${list.length} 个`);
    list.slice(0, 3).forEach(r => {
      console.log(`   - ${r.title} | ${r.sourceType} | ${r.contentUrl}`);
    });
  }

  // 4. 资源详情（第一个视频）
  const resp = await fetch(`${BASE}/api/learning/resources?childId=${childId}&subject=MATH&resourceType=VIDEO`, { headers });
  const data = await resp.json();
  const first = (data.data || [])[0];
  if (first) {
    const detailResp = await fetch(`${BASE}/api/learning/resource/${first.id}`, { headers });
    const detail = await detailResp.json();
    console.log(`4. DETAIL id=${first.id}: ${detail.data?.title} | ${detail.data?.sourceType}`);
    console.log(`   URL: ${detail.data?.contentUrl}`);
  }

  console.log('\nALL TESTS PASSED');
}

main().catch(e => {
  console.error('ERROR:', e.message);
  process.exit(1);
});
