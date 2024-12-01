interface UserPost {
  image: string
  content: string
}

interface Post extends UserPost{
  id: string
  idUser: string
  createdAt: number
}

export type {
  UserPost,
  Post
}