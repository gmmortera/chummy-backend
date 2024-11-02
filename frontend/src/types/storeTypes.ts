interface User {
  id: string,
  email: string,
  password: string
}

interface UserForm extends User{
  confirmation: string
}

type NewUser = Omit<User, "id">

export type {
  User,
  UserForm,
  NewUser
}