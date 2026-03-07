# ---------- Builder ----------
FROM golang:1.25-alpine AS builder

WORKDIR /app

COPY api/go.mod api/go.sum ./

RUN --mount=type=cache,target=/go/pkg/mod \
    go mod download

COPY api/ .

RUN --mount=type=cache,target=/root/.cache/go-build \
    --mount=type=cache,target=/go/pkg/mod \
    CGO_ENABLED=0 \
    go build -ldflags="-s -w" -o app

# ---------- Runtime ----------
FROM gcr.io/distroless/static-debian12

WORKDIR /app

COPY --from=builder /app/app .

USER nonroot:nonroot

EXPOSE 8080

ENTRYPOINT ["./app"]