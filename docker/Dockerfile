# ---------- Builder ----------
FROM golang:1.24 AS builder

# Ensure we have the tools to compile C code
RUN apt-get update && apt-get install -y gcc libc6-dev

WORKDIR /app
COPY api/go.mod api/go.sum ./
RUN go mod download

COPY api/ .

# The magic is in the -extldflags '-static'
RUN --mount=type=cache,target=/root/.cache/go-build \
    CGO_ENABLED=1 \
    go build -ldflags="-s -w -extldflags '-static'" -o app

RUN mkdir -p /app/data

# ---------- Runtime ----------
# Use 'base' or 'cc' to ensure basic C compatibility layer is present
FROM gcr.io/distroless/cc-debian12

WORKDIR /app
COPY --from=builder /app/app .
COPY --from=builder /app/data /app/data

# Ensure the nonroot user has permission to write to the sqlite DB
USER root
RUN chown -R nonroot:nonroot /app/data
USER nonroot:nonroot

ENTRYPOINT ["./app"]