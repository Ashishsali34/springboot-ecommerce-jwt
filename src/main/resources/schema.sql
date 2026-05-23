--create table users(username varchar_ignorecase(50) not null primary key,password varchar_ignorecase(500) not null,enabled boolean not null);
--create table authorities (username varchar_ignorecase(50) not null,authority varchar_ignorecase(50) not null,constraint fk_authorities_users foreign key(username) references users(username));
--create unique index ix_auth_username on authorities (username,authority);


// --- MongoDB schema equivalent of the given SQL DDL ---
// Run with: mongosh --file schema.sql

// 1) USERS collection
db.createCollection("users", {
  // Collation for case-insensitive comparisons (similar to varchar_ignorecase)
  collation: { locale: "en", strength: 2 },
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["username", "password", "enabled"],
      additionalProperties: false,
      properties: {
        _id: {},
        username: {
          bsonType: "string",
          description: "required string (max 50, case-insensitive unique)",
          maxLength: 50
        },
        password: {
          bsonType: "string",
          description: "required string (max 500)",
          maxLength: 500
        },
        enabled: {
          bsonType: "bool",
          description: "required boolean"
        }
      }
    }
  }
});

// Unique username (analogous to PRIMARY KEY on username)
db.users.createIndex(
  { username: 1 },
  { unique: true, name: "ux_users_username" }
);


// 2) AUTHORITIES collection
db.createCollection("authorities", {
  // Use the same case-insensitive collation to mirror varchar_ignorecase
  collation: { locale: "en", strength: 2 },
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["username", "authority"],
      additionalProperties: false,
      properties: {
        _id: {},
        username: {
          bsonType: "string",
          description: "required string (max 50)",
          maxLength: 50
        },
        authority: {
          bsonType: "string",
          description: "required string (max 50)",
          maxLength: 50
        }
      }
    }
  }
});

// Composite unique index (username, authority)
// Mirrors: create unique index ix_auth_username on authorities (username,authority);
db.authorities.createIndex(
  { username: 1, authority: 1 },
  { unique: true, name: "ix_auth_username" }
);


// NOTE: MongoDB has no FOREIGN KEY constraints.
// If you need to ensure authorities.username exists in users,
// enforce it in application logic or with transactions during writes.
``