# ---------- Builder ----------
FROM golang:1.24-alpine AS builder

# 1. Install build tools for CGO
RUN apk add --no-cache gcc musl-dev

WORKDIR /app
COPY api/go.mod api/go.sum ./
RUN go mod download

COPY api/ .

# 2. Enable CGO and build
RUN --mount=type=cache,target=/root/.cache/go-build \
    CGO_ENABLED=1 \
    go build -ldflags="-s -w" -o app

# ---------- Runtime ----------
# 3. Switch from 'static' to 'base' (includes libc)
FROM gcr.io/distroless/base-debian12

WORKDIR /app
COPY --from=builder /app/app .

# 4. Handle Permissions (Distroless nonroot is UID 65532)
# We create a directory that the nonroot user can actually write to
USER root
RUN mkdir /app/data && chown 65532:65532 /app/data
USER nonroot:nonroot

EXPOSE 8080
ENTRYPOINT ["./app"]