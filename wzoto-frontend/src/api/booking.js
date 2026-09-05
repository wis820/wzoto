import request from '@/utils/request'

export function createBooking(data) {
  return request.post('/booking', data)
}

export function confirmBooking(id) {
  return request.post(`/booking/${id}/confirm`)
}

export function rejectBooking(id, reason) {
  return request.post(`/booking/${id}/reject`, null, { params: { reason } })
}

export function cancelBooking(id) {
  return request.post(`/booking/${id}/cancel`)
}

export function getMyBookings() {
  return request.get('/booking/my')
}

export function getBookingById(id) {
  return request.get(`/booking/${id}`)
}