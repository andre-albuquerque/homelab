# ---------- Builder ----------
FROM golang:1.24 AS builder

RUN apt-get update && apt-get install -y gcc libc6-dev

WORKDIR /app
COPY api/go.mod api/go.sum ./
RUN go mod download

COPY api/ .

RUN --mount=type=cache,target=/root/.cache/go-build \
    CGO_ENABLED=1 \
    go build -ldflags="-s -w" -o app

RUN mkdir -p /app/data

# ---------- Runtime ----------
FROM gcr.io/distroless/cc-debian12

WORKDIR /app
COPY --from=builder /app/app .
COPY --from=builder /app/data /app/data

USER nonroot:nonroot
ENTRYPOINT ["./app"]