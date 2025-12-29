export interface ProseMirrorDoc {
    type: 'doc'
    content: ProseMirrorNode[]
}

export interface ProseMirrorNode {
    type: string
    attrs?: Record<string, any>
    content?: ProseMirrorNode[]
    text?: string
}
