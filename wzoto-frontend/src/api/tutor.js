import request from '@/utils/request'

export function createTutorProfile(data) {
  return request.post('/tutor-profile', data)
}

export function updateTutorProfile(data) {
  return request.put('/tutor-profile', data)
}

export function toggleTutorActive() {
  return request.post('/tutor-profile/toggle-active')
}

export function getMyTutorProfile() {
  return request.get('/tutor-profile/mine')
}

export function searchTutors(params) {
  return request.get('/tutor-profile/search', { params })
}

export function getTutorProfileById(id) {
  return request.get(`/tutor-profile/${id}`)
}