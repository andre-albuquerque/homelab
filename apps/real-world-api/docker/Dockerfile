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
# Set the numeric ID for nonroot (usually 65532 in distroless)
RUN chown -R 65532:65532 /app/data

# ---------- Runtime ----------
FROM gcr.io/distroless/cc-debian12

WORKDIR /app
# Use --chown to set ownership during the copy process
COPY --from=builder --chown=nonroot:nonroot /app/app .
COPY --from=builder --chown=nonroot:nonroot /app/data /app/data

USER nonroot:nonroot
ENTRYPOINT ["./app"]